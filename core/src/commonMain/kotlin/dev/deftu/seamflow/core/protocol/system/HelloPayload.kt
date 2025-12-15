package dev.deftu.seamflow.core.protocol.system

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.DeviceType
import dev.deftu.seamflow.core.protocol.Envelope
import dev.deftu.seamflow.core.protocol.feature.FeatureDescriptor
import dev.deftu.seamflow.core.protocol.link.Link
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
@SerialName("hello")
data class HelloPayload(
    val deviceId: DeviceId,
    val deviceName: String,
    val deviceType: DeviceType,
    val features: Set<FeatureDescriptor>,
    val supportedLinks: Set<Link.Type>,
) {
    companion object {
        fun build(
            json: Json,
            localDeviceId: DeviceId,
            payload: HelloPayload,
            targetDeviceId: DeviceId? = null
        ): Envelope {
            val json = json.encodeToJsonElement(serializer(), payload)

            return Envelope(
                id = Envelope.id(),
                sourceDeviceId = localDeviceId,
                targetDeviceId = targetDeviceId,
                feature = SystemProtocol.FEATURE_SYSTEM,
                type = SystemProtocol.TYPE_HELLO,
                payload = json
            )
        }
    }
}
