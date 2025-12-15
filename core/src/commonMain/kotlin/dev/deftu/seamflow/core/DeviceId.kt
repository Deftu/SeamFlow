package dev.deftu.seamflow.core

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class DeviceId(val value: String) {
    companion object {
        const val UNKNOWN_NAME = "Unknown Device"
        const val UNKNOWN_HOST = "unknown-host"

        val Unknown = DeviceId("unknown")

        fun populate(userName: String, hostName: String): DeviceId {
            val normalizedUserName = userName.ifBlank { UNKNOWN_NAME }.trim().lowercase()
            val normalizedHostName = hostName.ifBlank { UNKNOWN_HOST }.trim().lowercase()
            val deviceId = "$normalizedUserName@$normalizedHostName"
            return DeviceId(deviceId)
        }
    }

    init {
        require(value.isNotEmpty()) { "Device ID cannot be empty" }
    }
}
