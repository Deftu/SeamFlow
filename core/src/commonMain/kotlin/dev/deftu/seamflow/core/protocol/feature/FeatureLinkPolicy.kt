package dev.deftu.seamflow.core.protocol.feature

import dev.deftu.seamflow.core.protocol.link.Link
import kotlinx.serialization.Serializable

@Serializable
data class FeatureLinkPolicy(
    val allowedTypes: Set<Link.Type>,
    val preferredTypes: Set<Link.Type> = emptySet(),
) {
    companion object {
        val All = FeatureLinkPolicy(Link.Type.entries.toSet())
    }

    fun allows(type: Link.Type): Boolean {
        return allowedTypes.contains(type)
    }
}
