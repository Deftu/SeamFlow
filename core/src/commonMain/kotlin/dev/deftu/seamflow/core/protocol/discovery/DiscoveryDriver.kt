package dev.deftu.seamflow.core.protocol.discovery

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.protocol.link.Link
import kotlinx.coroutines.flow.StateFlow

interface DiscoveryDriver {
    val peers: StateFlow<Map<Link.Type, Map<DeviceId, Peer>>>

    fun register(type: Link.Type, discovery: PeerDiscovery)

    suspend fun start()
    suspend fun stop()
}
