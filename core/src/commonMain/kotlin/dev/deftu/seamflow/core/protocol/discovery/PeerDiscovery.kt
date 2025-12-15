package dev.deftu.seamflow.core.protocol.discovery

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.protocol.link.Link
import kotlinx.coroutines.flow.StateFlow

interface PeerDiscovery {
    val type: Link.Type
    val peers: StateFlow<Map<DeviceId, Peer>>

    suspend fun start()
    suspend fun stop()
}
