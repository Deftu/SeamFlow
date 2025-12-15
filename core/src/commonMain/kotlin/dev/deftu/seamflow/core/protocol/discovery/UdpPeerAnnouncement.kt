package dev.deftu.seamflow.core.protocol.discovery

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.DeviceType
import dev.deftu.seamflow.core.protocol.link.Link
import io.ktor.network.sockets.InetSocketAddress
import kotlinx.serialization.Serializable

@Serializable
data class UdpPeerAnnouncement(
    val deviceId: DeviceId,
    val deviceName: String,
    val deviceType: DeviceType,
    val hostEpochMs: Long,
    val sessionPort: Int?,
    val capabilities: Set<Link.Capability>,
    val supportedLinks: Set<Link.Type>,
) {
    fun asPeer(
        type: Link.Type,
        incomingAddress: InetSocketAddress,
        lastSeenAtMs: Long,
    ): Peer {
        return Peer(
            deviceId = deviceId,
            deviceName = deviceName,
            deviceType = deviceType,
            type = type,
            capabilities = capabilities,
            endpoint = Endpoint.Ip(incomingAddress.hostname, incomingAddress.port),
            hostedPort = sessionPort,
            hostEpochMs = hostEpochMs,
            lastSeenAtMs = lastSeenAtMs,
        )
    }
}
