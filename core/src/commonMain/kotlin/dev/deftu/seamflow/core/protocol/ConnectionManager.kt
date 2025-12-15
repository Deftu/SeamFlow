package dev.deftu.seamflow.core.protocol

import dev.deftu.seamflow.core.protocol.feature.Feature
import dev.deftu.seamflow.core.protocol.link.Link
import dev.deftu.seamflow.core.protocol.session.Session
import kotlinx.coroutines.flow.StateFlow

interface ConnectionManager {
    val sessions: StateFlow<List<Session>>

    fun register(feature: Feature)
    suspend fun accept(link: Link)

    suspend fun start()
    suspend fun stop()
}
