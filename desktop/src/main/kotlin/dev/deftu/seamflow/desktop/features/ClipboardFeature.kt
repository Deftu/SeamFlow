package dev.deftu.seamflow.desktop.features

import dev.deftu.seamflow.core.feature.clipboard.ClipboardProtocol
import dev.deftu.seamflow.core.feature.clipboard.ClipboardRequestPayload
import dev.deftu.seamflow.core.feature.clipboard.ClipboardSetPayload
import dev.deftu.seamflow.core.obtainLocalDeviceInfo
import dev.deftu.seamflow.core.protocol.Envelope
import dev.deftu.seamflow.core.protocol.feature.Feature
import dev.deftu.seamflow.core.protocol.feature.FeatureId
import dev.deftu.seamflow.core.protocol.feature.FeatureLinkPolicy
import dev.deftu.seamflow.core.protocol.session.Session
import dev.deftu.seamflow.desktop.clipboard.DesktopClipboard
import kotlinx.serialization.json.Json

class ClipboardFeature(
    private val json: Json,
    private val clipboard: DesktopClipboard
) : Feature {
    override val name: FeatureId = ClipboardProtocol.FEATURE_CLIPBOARD
    override val linkPolicy: FeatureLinkPolicy = FeatureLinkPolicy.All

    override suspend fun handleMessage(session: Session, envelope: Envelope) {
        when (envelope.type) {
            ClipboardProtocol.TYPE_SET -> handleSet(envelope)
            ClipboardProtocol.TYPE_REQUEST -> handleRequest(session, envelope)
        }
    }

    private fun handleSet(envelope: Envelope) {
        val payload = runCatching {
            json.decodeFromJsonElement(ClipboardSetPayload.serializer(), envelope.payload)
        }.getOrElse {
            println("Invalid clipboard set payload: $it")
            return
        }

        val current = clipboard.readTextOrNull()
        if (current == payload.content) {
            return
        }

        clipboard.writeText(payload.content)
    }

    private suspend fun handleRequest(session: Session, envelope: Envelope) {
        runCatching {
            json.decodeFromJsonElement(ClipboardRequestPayload.serializer(), envelope.payload)
        }.getOrElse {
            println("Invalid clipboard request payload: $it")
            return
        }

        val text = clipboard.readTextOrNull() ?: return
        val localInfo = obtainLocalDeviceInfo()

        val set = ClipboardSetPayload(text)
        session.send(ClipboardSetPayload.build(json, localInfo.id, session.state.value.deviceId, set))
    }
}
