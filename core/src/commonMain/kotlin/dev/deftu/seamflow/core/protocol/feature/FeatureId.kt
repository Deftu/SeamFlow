package dev.deftu.seamflow.core.protocol.feature

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class FeatureId(val value: String) {
    init {
        require(value.isNotBlank()) { "Feature ID cannot be blank" }
    }

    override fun toString(): String {
        return value
    }
}
