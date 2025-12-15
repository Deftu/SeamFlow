package dev.deftu.seamflow.core.protocol.session

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.protocol.feature.FeatureDescriptor
import dev.deftu.seamflow.core.protocol.link.LinkInfo

data class SessionState(
    val deviceId: DeviceId,
    val deviceName: String,
    val isConnected: Boolean,
    val activeLink: LinkInfo,
    val remoteFeatures: Set<FeatureDescriptor>,
    val lastSeenMs: Long,
    val lastRttMs: Long?,
) {
    companion object {
        fun initial(linkInfo: LinkInfo): SessionState {
            return SessionState(
                deviceId = DeviceId.Unknown,
                deviceName = DeviceId.UNKNOWN_NAME,
                isConnected = true,
                activeLink = linkInfo,
                remoteFeatures = emptySet(),
                lastSeenMs = System.currentTimeMillis(),
                lastRttMs = null,
            )
        }
    }

    fun hi(deviceId: DeviceId, deviceName: String): SessionState {
        return copy(deviceId = deviceId, deviceName = deviceName)
    }

    fun seen(nowMs: Long): SessionState {
        return copy(lastSeenMs = nowMs)
    }

    fun rtt(ms: Long, nowMs: Long): SessionState {
        return copy(lastRttMs = ms, lastSeenMs = nowMs)
    }

    fun linkTo(linkInfo: LinkInfo): SessionState {
        return copy(activeLink = linkInfo)
    }

    fun disconnect(): SessionState {
        return copy(isConnected = false)
    }
}
