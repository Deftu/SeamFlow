package dev.deftu.seamflow.core.protocol.link

import io.ktor.websocket.CloseReason
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

class WebSocketLink(
    private val session: DefaultWebSocketSession,
    override val info: LinkInfo,
) : Link {
    override val incoming: Flow<String> =
        session.incoming
            .receiveAsFlow()
            .filterIsInstance<Frame.Text>()
            .map(Frame.Text::readText)

    override suspend fun send(text: String) {
        session.send(Frame.Text(text))
    }

    override suspend fun close(cause: Throwable?) {
        val reason = cause?.asCloseReason() ?: CloseReason(CloseReason.Codes.NORMAL, "Closed by peer")
        runCatching { session.close(reason) }
    }

    private fun Throwable.asCloseReason(): CloseReason {
        return CloseReason(CloseReason.Codes.INTERNAL_ERROR, message ?: "Unknown error")
    }
}
