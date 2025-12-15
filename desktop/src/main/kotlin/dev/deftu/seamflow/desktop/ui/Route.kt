package dev.deftu.seamflow.desktop.ui

import dev.deftu.seamflow.core.DeviceId
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable data object Home : Route
    @Serializable data object Settings : Route
    @Serializable data object Discovery : Route
    @Serializable data object Transfers : Route
    @Serializable data object Messages : Route
    @Serializable data class PeerDetails(val id: String) : Route {
        fun asDeviceId(): DeviceId {
            return DeviceId(id)
        }
    }
}
