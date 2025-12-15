package dev.deftu.seamflow.core.protocol.discovery

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.DeviceType
import dev.deftu.seamflow.core.protocol.link.Link

data class Peer(
    val deviceId: DeviceId,
    val deviceName: String,
    val deviceType: DeviceType,
    val type: Link.Type,
    val capabilities: Set<Link.Capability>,
    val endpoint: Endpoint,
    val hostedPort: Int?,
    val hostEpochMs: Long,
    val lastSeenAtMs: Long,
)
