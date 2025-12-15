package dev.deftu.seamflow.core.protocol.feature

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeatureDescriptor(
    val name: String,
    val direction: FeatureDirection,
    val version: Int,
) {
    @Serializable
    enum class FeatureDirection {
        @SerialName("send") SEND,
        @SerialName("receive") RECEIVE,
        @SerialName("both") BIDIRECTIONAL
    }
}
