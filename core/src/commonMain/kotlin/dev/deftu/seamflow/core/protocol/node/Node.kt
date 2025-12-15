package dev.deftu.seamflow.core.protocol.node

import dev.deftu.seamflow.core.protocol.ConnectionManager

interface Node {
    enum class Capability {
        LISTEN,
        DIAL,
        ACCESS_POINT,
        ;
    }

    val connectionManager: ConnectionManager

    suspend fun start()
    suspend fun stop()
}
