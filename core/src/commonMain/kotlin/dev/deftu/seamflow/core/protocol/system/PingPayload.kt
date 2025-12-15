package dev.deftu.seamflow.core.protocol.system

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.protocol.Envelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
@SerialName("ping")
data class PingPayload(val nonce: String) {
    companion object {
        fun build(json: Json, localDeviceId: DeviceId, targetDeviceId: DeviceId, payload: PingPayload): Envelope {
            val json = json.encodeToJsonElement(serializer(), payload)

            return Envelope(
                id = Envelope.id(),
                sourceDeviceId = localDeviceId,
                targetDeviceId = targetDeviceId,
                feature = SystemProtocol.FEATURE_SYSTEM,
                type = SystemProtocol.TYPE_PING,
                payload = json
            )
        }
    }
}
