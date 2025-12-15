package dev.deftu.seamflow.core.protocol.system

import dev.deftu.seamflow.core.protocol.MessageType
import dev.deftu.seamflow.core.protocol.feature.FeatureId

object SystemProtocol {
    val FEATURE_SYSTEM = FeatureId("system")

    val TYPE_ERROR = MessageType("error")
    val TYPE_HELLO = MessageType("hello")
    val TYPE_PING = MessageType("ping")
    val TYPE_PONG = MessageType("pong")
}
