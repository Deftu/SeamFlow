package dev.deftu.seamflow.core.protocol.discovery

sealed interface Endpoint {
    data class Ip(val address: String, val port: Int) : Endpoint
    data class Bluetooth(val mac: String) : Endpoint
}
