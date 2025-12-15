package dev.deftu.seamflow.core.feature.clipboard

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.protocol.Envelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
@SerialName("clipboard_request")
data class ClipboardRequestPayload(val type: ClipboardContentType) {
    companion object {
        fun build(
            json: Json,
            localDeviceId: DeviceId,
            targetDeviceId: DeviceId,
            payload: ClipboardRequestPayload,
        ): Envelope {
            val json = json.encodeToJsonElement(serializer(), payload)

            return Envelope(
                id = Envelope.id(),
                sourceDeviceId = localDeviceId,
                targetDeviceId = targetDeviceId,
                feature = ClipboardProtocol.FEATURE_CLIPBOARD,
                type = ClipboardProtocol.TYPE_REQUEST,
                payload = json
            )
        }
    }
}
