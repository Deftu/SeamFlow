package dev.deftu.seamflow.core

import dev.deftu.seamflow.core.protocol.link.Link
import oshi.SystemInfo

actual fun obtainLocalDeviceInfo(): LocalDeviceInfo {
    val userName = System.getProperty("user.name") ?: "Unknown User"
    val hostName = System.getenv("HOSTNAME")
        ?: System.getenv("COMPUTERNAME")
        ?: "Unknown Host"

    val hasBattery = detectBatteryPresence()
    val deviceType = if (hasBattery) DeviceType.LAPTOP else DeviceType.DESKTOP

    return LocalDeviceInfo(
        id = DeviceId.populate(userName, hostName),
        name = hostName,
        type = deviceType,
        supportedLinks = Link.Type.All,
        supportedFeatures = emptySet(),
    )
}

private fun detectBatteryPresence(): Boolean {
    return try {
        val si = SystemInfo()
        val powerSources = si.hardware.powerSources
        // If it reports at least one power source that looks like a battery, treat as laptop.
        powerSources.isNotEmpty()
    } catch (_: Throwable) {
        false
    }
}
