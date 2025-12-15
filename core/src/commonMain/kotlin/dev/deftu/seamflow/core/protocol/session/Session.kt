package dev.deftu.seamflow.core.protocol.session

import dev.deftu.seamflow.core.protocol.Envelope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Session {
    val id: String
    val state: StateFlow<SessionState>
    val incoming: Flow<List<Envelope>>

    suspend fun send(envelope: Envelope)
    suspend fun close(cause: Throwable? = null)
}
