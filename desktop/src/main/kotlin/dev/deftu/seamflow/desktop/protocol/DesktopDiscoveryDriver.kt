package dev.deftu.seamflow.desktop.protocol

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.protocol.discovery.DiscoveryDriver
import dev.deftu.seamflow.core.protocol.discovery.Peer
import dev.deftu.seamflow.core.protocol.discovery.PeerDiscovery
import dev.deftu.seamflow.core.protocol.link.Link
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DesktopDiscoveryDriver : DiscoveryDriver {
    private val discoverers = linkedMapOf<Link.Type, PeerDiscovery>()

    private var job = SupervisorJob()
    private var scope = CoroutineScope(job + Dispatchers.Default)

    override val peers: StateFlow<Map<Link.Type, Map<DeviceId, Peer>>> get() = _peers

    private val _peers = MutableStateFlow<Map<Link.Type, Map<DeviceId, Peer>>>(emptyMap())

    override fun register(type: Link.Type, discovery: PeerDiscovery) {
        discoverers[type] = discovery
    }

    override suspend fun start() {
        if (!job.isActive) {
            job = SupervisorJob()
            scope = CoroutineScope(job + Dispatchers.Default)
        }

        for (discovery in discoverers.values) {
            discovery.start()

            scope.launch {
                discovery.peers.collectLatest { byId ->
                    _peers.update { current ->
                        val mutable = current.toMutableMap()
                        mutable[discovery.type] = byId
                        mutable.toMap()
                    }
                }
            }
        }
    }

    override suspend fun stop() {
        for (discovery in discoverers.values) {
            runCatching { discovery.stop() }
        }

        scope.cancel()
        _peers.value = emptyMap()
    }
}
