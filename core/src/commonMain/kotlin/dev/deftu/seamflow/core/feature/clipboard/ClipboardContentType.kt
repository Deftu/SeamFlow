package dev.deftu.seamflow.core.feature.clipboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ClipboardContentType {
    @SerialName("text") TEXT,
    @SerialName("image") IMAGE,
    @SerialName("html") HTML,
    @SerialName("rich_text") RTF,
    @SerialName("file") FILE,
}
