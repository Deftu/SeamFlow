package dev.deftu.seamflow.desktop.protocol

import dev.deftu.seamflow.core.protocol.ConnectionManager
import dev.deftu.seamflow.core.protocol.link.Link
import dev.deftu.seamflow.core.protocol.link.LinkInfo
import dev.deftu.seamflow.core.protocol.link.WebSocketLink
import dev.deftu.seamflow.core.protocol.node.Node
import dev.deftu.seamflow.desktop.clipboard.DesktopClipboard
import dev.deftu.seamflow.desktop.features.ClipboardFeature
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import kotlinx.serialization.json.Json

class DesktopNode : Node {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override val connectionManager: ConnectionManager
        get() = _connectionManager

    private val _connectionManager = DesktopConnectionManager(json)

    private var engine: EmbeddedServer<*, *>? = null

    override suspend fun start() {
        if (engine != null) {
            return
        }

        _connectionManager.register(ClipboardFeature(json, DesktopClipboard()))
        _connectionManager.start()

        engine = embeddedServer(CIO, port = 5050) {
            install(WebSockets)

            routing {
                webSocket("/control") {
                    val link = WebSocketLink(this, LinkInfo(Link.Type.LAN, Link.Capability.All))

                    try {
                        _connectionManager.accept(link)
                    } finally {
                        runCatching { link.close() }
                    }
                }
            }
        }.start(wait = false)
    }

    override suspend fun stop() {
        engine?.stop(1000, 2_000)
        engine = null

        _connectionManager.stop()
    }
}
