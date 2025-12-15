package dev.deftu.seamflow.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class DeviceType {
    @SerialName("desktop") DESKTOP,
    @SerialName("laptop") LAPTOP,
    @SerialName("mobile") MOBILE,
    @SerialName("web") WEB,
    @SerialName("other") OTHER,
    ;

    val isComputer: Boolean
        get() = this == DESKTOP || this == LAPTOP

    val isMobile: Boolean
        get() = this == MOBILE

    val isWeb: Boolean
        get() = this == WEB
}
