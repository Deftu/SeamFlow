package dev.deftu.seamflow.core.protocol

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class MessageType(val value: String) {
    init {
        require(value.isNotBlank()) { "Message type cannot be blank" }
        require(!value.contains(" ")) { "Message type cannot contain spaces" }
        require(value == value.lowercase()) { "Message type must be lowercase" }

        require(value.toCharArray().all { it.isLetterOrDigit() || it == '_' || it == '-' }) {
            "Message type can only contain letters, digits, underscores, and hyphens"
        }
    }

    override fun toString(): String {
        return value
    }
}
