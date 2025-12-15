package dev.deftu.seamflow.core.protocol.feature

import dev.deftu.seamflow.core.protocol.Envelope
import dev.deftu.seamflow.core.protocol.session.Session

interface Feature {
    val name: FeatureId
    val linkPolicy: FeatureLinkPolicy

    suspend fun handleConnect(session: Session) {  }
    suspend fun handleDisconnect(session: Session) {  }

    suspend fun handleMessage(session: Session, envelope: Envelope) {  }
}
