package dev.deftu.seamflow.desktop.protocol

import dev.deftu.seamflow.core.LocalDeviceInfo
import dev.deftu.seamflow.core.obtainLocalDeviceInfo
import dev.deftu.seamflow.core.protocol.ConnectionManager
import dev.deftu.seamflow.core.protocol.Envelope
import dev.deftu.seamflow.core.protocol.feature.Feature
import dev.deftu.seamflow.core.protocol.feature.FeatureId
import dev.deftu.seamflow.core.protocol.link.Link
import dev.deftu.seamflow.core.protocol.session.Session
import dev.deftu.seamflow.core.protocol.system.ErrorPayload
import dev.deftu.seamflow.core.protocol.system.HelloPayload
import dev.deftu.seamflow.core.protocol.system.PingPayload
import dev.deftu.seamflow.core.protocol.system.SystemProtocol
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.random.Random

class DesktopConnectionManager(private val json: Json) : ConnectionManager {
    private var job = SupervisorJob()
    private var scope = CoroutineScope(job + Dispatchers.Default)

    override val sessions: StateFlow<List<Session>>
        get() = _sessions

    private val _sessions = MutableStateFlow(emptyList<Session>())
    private val supportedFeatures = linkedMapOf<FeatureId, Feature>()
    private var pingerJob: Job? = null

    override fun register(feature: Feature) {
        supportedFeatures[feature.name] = feature
    }

    override suspend fun start() {
        if (!job.isActive) {
            job = SupervisorJob()
            scope = CoroutineScope(job + Dispatchers.Default)
        }

        if (pingerJob == null || pingerJob?.isCompleted == true) {
            pingerJob = scope.launch {
                while (isActive) {
                    delay(5_000)

                    val localInfo = obtainLocalDeviceInfo()
                    val nowMs = System.currentTimeMillis()

                    val snapshot = _sessions.value
                    for (session in snapshot) {
                        val desktop = session as? DesktopSession ?: continue
                        val state = desktop.state.value
                        if (!state.isConnected) {
                            continue
                        }

                        val nonce = createNonce()
                        desktop.ping(nonce, nowMs)
                        runCatching {
                            val ping = PingPayload(nonce)
                            session.send(PingPayload.build(json, localInfo.id, state.deviceId, ping))
                        }.onFailure {
                            // TODO
                        }
                    }
                }
            }
        }
    }

    override suspend fun stop() {
        pingerJob?.cancel()
        pingerJob = null

        job.cancel()
        _sessions.value = emptyList()
    }

    override suspend fun accept(link: Link) {
        val localInfo = obtainLocalDeviceInfo()
        val session = DesktopSession("desktop-${System.identityHashCode(link)}", link, json)

        try {
            _sessions.update { it + session }

            val hello = HelloPayload(localInfo.id, localInfo.name, localInfo.type, localInfo.supportedFeatures, localInfo.supportedLinks)
            session.send(HelloPayload.build(json, localInfo.id, hello))

            link.incoming.collect {
                scope.ensureActive()

                val envelope = try {
                    json.decodeFromString(Envelope.serializer(), it)
                } catch (e: Exception) {
                    session.send(ErrorPayload.build(json, localInfo.id, ErrorPayload(e.message ?: "Unknown error")))
                    return@collect
                }

                // Handle system messages
                if (envelope.feature == SystemProtocol.FEATURE_SYSTEM) {
                    when (envelope.type) {
                        SystemProtocol.TYPE_HELLO -> hi(localInfo, session, envelope)
                        SystemProtocol.TYPE_PING -> ping(localInfo, session, envelope)
                        SystemProtocol.TYPE_PONG -> pong(localInfo, session, envelope)

                        else -> {
                            session.send(ErrorPayload.build(json, localInfo.id, ErrorPayload("Unknown system message type: ${envelope.type}")))
                        }
                    }

                    return@collect
                }

                // Allow the session to process the message
                session.deploy(envelope)

                // Allow whatever necessary feature process the message
                supportedFeatures[envelope.feature]?.let { feature ->
                    if (feature.linkPolicy.allows(session.state.value.activeLink.type)) {
                        scope.launch {
                            feature.handleMessage(session, envelope)
                        }
                    }
                }
            }
        } finally {
            _sessions.update { it - session }
        }
    }

    private suspend fun hi(localInfo: LocalDeviceInfo, session: DesktopSession, envelope: Envelope) {
        val payload = try {
            json.decodeFromJsonElement(HelloPayload.serializer(), envelope.payload)
        } catch (e: Exception) {
            session.send(ErrorPayload.build(json, localInfo.id, ErrorPayload("Invalid Hello payload: ${e.message ?: "Unknown error"}")))
            return
        }

        session.hi(payload.deviceId, payload.deviceName)
    }

    private suspend fun ping(localInfo: LocalDeviceInfo, session: DesktopSession, envelope: Envelope) {
        val payload = try {
            json.decodeFromJsonElement(PingPayload.serializer(), envelope.payload)
        } catch (e: Exception) {
            session.send(ErrorPayload.build(json, localInfo.id, ErrorPayload("Invalid Ping payload: ${e.message ?: "Unknown error"}")))
            return
        }

        val pong = PingPayload(payload.nonce)
        session.send(PingPayload.build(json, localInfo.id, envelope.sourceDeviceId, pong))
    }

    private suspend fun pong(localInfo: LocalDeviceInfo, session: DesktopSession, envelope: Envelope) {
        val payload = try {
            json.decodeFromJsonElement(PingPayload.serializer(), envelope.payload)
        } catch (e: Exception) {
            session.send(ErrorPayload.build(json, localInfo.id, ErrorPayload("Invalid Pong payload: ${e.message ?: "Unknown error"}")))
            return
        }

        session.pong(payload.nonce, System.currentTimeMillis())
    }

    private fun createNonce(): String {
        val bytes = ByteArray(16)
        Random.nextBytes(bytes)
        return bytes.joinToString("") { b ->
            ((b.toInt() and 0xFF) + 0x100).toString(16).substring(1)
        }
    }
}
