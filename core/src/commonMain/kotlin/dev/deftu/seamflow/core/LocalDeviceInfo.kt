package dev.deftu.seamflow.core

import dev.deftu.seamflow.core.protocol.feature.FeatureDescriptor
import dev.deftu.seamflow.core.protocol.link.Link

data class LocalDeviceInfo(
    val id: DeviceId,
    val name: String,
    val type: DeviceType,
    val supportedLinks: Set<Link.Type>,
    val supportedFeatures: Set<FeatureDescriptor>,
)

expect fun obtainLocalDeviceInfo(): LocalDeviceInfo
