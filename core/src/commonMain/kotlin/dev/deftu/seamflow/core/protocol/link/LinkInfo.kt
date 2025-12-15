package dev.deftu.seamflow.core.protocol.link

data class LinkInfo(
    val type: Link.Type,
    val capabilities: Set<Link.Capability>,
    val maxRecommendedPayloadBytes: Long? = null,
) {
    init {
        require(maxRecommendedPayloadBytes == null || maxRecommendedPayloadBytes > 0) { "maxRecommendedPayloadBytes must be null or greater than 0" }
    }
}
