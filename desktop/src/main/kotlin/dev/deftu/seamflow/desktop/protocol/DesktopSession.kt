package dev.deftu.seamflow.desktop.protocol

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.protocol.Envelope
import dev.deftu.seamflow.core.protocol.link.Link
import dev.deftu.seamflow.core.protocol.link.LinkInfo
import dev.deftu.seamflow.core.protocol.session.Session
import dev.deftu.seamflow.core.protocol.session.SessionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

class DesktopSession(
    override val id: String,
    private val link: Link,
    private val json: Json,
) : Session {
    override val state: StateFlow<SessionState> get() = _state

    override val incoming: Flow<List<Envelope>> get() = _incoming

    private val _state = MutableStateFlow(SessionState.initial(link.info))
    private val _incoming = MutableStateFlow(emptyList<Envelope>())
    private val pingSentAtMs = linkedMapOf<String, Long>()

    override suspend fun send(envelope: Envelope) {
        link.send(json.encodeToString(Envelope.serializer(), envelope))
    }

    override suspend fun close(cause: Throwable?) {
        link.close(cause)
        _state.update(SessionState::disconnect)
    }

    fun deploy(envelope: Envelope) {
        _incoming.update { it + envelope }
    }

    fun hi(deviceId: DeviceId, deviceName: String) {
        _state.update { state ->
            state.hi(deviceId, deviceName)
        }
    }

    fun traffic(nowMs: Long) {
        _state.update { state ->
            state.seen(nowMs)
        }
    }

    fun ping(nonce: String, nowMs: Long) {
        pingSentAtMs[nonce] = nowMs
    }

    fun pong(nonce: String, nowMs: Long): Long? {
        val sentAt = pingSentAtMs.remove(nonce) ?: return null
        val rtt = nowMs - sentAt
        _state.update { state ->
            state.rtt(rtt, nowMs)
        }

        return rtt
    }

    fun linkTo(linkInfo: LinkInfo) {
        _state.update { state ->
            state.linkTo(linkInfo)
        }
    }
}
