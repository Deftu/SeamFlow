package dev.deftu.seamflow.core.protocol.link

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Link {
    @Serializable
    enum class Type {
        @SerialName("lan") LAN,
        @SerialName("hotspot") HOTSPOT,
        @SerialName("direct") DIRECT,
        @SerialName("tether") TETHER,
        @SerialName("bluetooth") BLUETOOTH,
        ;

        companion object {
            val All = entries.toSet()
        }
    }

    @Serializable
    enum class Capability {
        /** Supports TCP/UDP (HTTP/WebSocket) transport */
        @SerialName("ip") IP,
        /** Supports high bandwidth / big data transfer */
        @SerialName("high_bw") HIGH_BANDWIDTH,
        /** Good for realtime-ish */
        @SerialName("low_latency") LOW_LATENCY,
        ;

        companion object {
            val All = entries.toSet()
        }
    }

    val info: LinkInfo
    val incoming: Flow<String>

    suspend fun send(text: String)
    suspend fun close(cause: Throwable? = null)
}
