package dev.deftu.seamflow.core.protocol

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.protocol.feature.FeatureId
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.util.UUID

@Serializable
data class Envelope(
    val id: String,
    val sourceDeviceId: DeviceId,
    val targetDeviceId: DeviceId?,
    val feature: FeatureId,
    val type: MessageType,
    val payload: JsonElement,
) {
    companion object {
        fun id(): String {
            return UUID.randomUUID().toString()
        }
    }

    init {
        require(id.isNotBlank()) { "Envelope id must not be blank" }
    }
}
