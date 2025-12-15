package dev.deftu.seamflow.core.feature.clipboard

import dev.deftu.seamflow.core.protocol.MessageType
import dev.deftu.seamflow.core.protocol.feature.FeatureId

object ClipboardProtocol {
    val FEATURE_CLIPBOARD = FeatureId("clipboard")

    val TYPE_SET = MessageType("set")
    val TYPE_REQUEST = MessageType("request")
}
