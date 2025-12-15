package dev.deftu.seamflow.core.protocol.system

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.protocol.Envelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
@SerialName("error")
data class ErrorPayload(val message: String) {
    companion object {
        fun build(
            json: Json,
            localDeviceId: DeviceId,
            payload: ErrorPayload,
            targetDeviceId: DeviceId? = null
        ): Envelope {
            val json = json.encodeToJsonElement(serializer(), payload)

            return Envelope(
                id = Envelope.id(),
                sourceDeviceId = localDeviceId,
                targetDeviceId = targetDeviceId,
                feature = SystemProtocol.FEATURE_SYSTEM,
                type = SystemProtocol.TYPE_ERROR,
                payload = json
            )
        }
    }
}
