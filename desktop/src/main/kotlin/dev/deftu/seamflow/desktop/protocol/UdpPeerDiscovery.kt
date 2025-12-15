package dev.deftu.seamflow.desktop.protocol

import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.obtainLocalDeviceInfo
import dev.deftu.seamflow.core.protocol.discovery.Peer
import dev.deftu.seamflow.core.protocol.discovery.PeerDiscovery
import dev.deftu.seamflow.core.protocol.discovery.UdpPeerAnnouncement
import dev.deftu.seamflow.core.protocol.link.Link
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.io.readByteArray
import kotlinx.serialization.json.Json

class UdpPeerDiscovery(
    private val json: Json,
    override val type: Link.Type,
    private val ttlMs: Long,
    private val hostEpochMs: Long,
    private val discoveryPort: Int = 5051,
    private val announceIntervalMs: Long = 1_000L,
) : PeerDiscovery {
    private val selectorManager = SelectorManager()

    private var job = SupervisorJob()
    private var scope = CoroutineScope(job + Dispatchers.IO)

    private var listenJob: Job? = null
    private var announceJob: Job? = null
    private var expireJob: Job? = null

    override val peers: StateFlow<Map<DeviceId, Peer>> get() = _peers

    private val _peers = MutableStateFlow<Map<DeviceId, Peer>>(emptyMap())

    override suspend fun start() {
        if (!job.isActive) {
            job = SupervisorJob()
            scope = CoroutineScope(job + Dispatchers.IO)
        }

        if (listenJob == null || listenJob?.isCompleted == true) {
            listenJob = scope.launch { listen() }
        }

        if (announceJob == null || announceJob?.isCompleted == true) {
            announceJob = scope.launch { announcePeriodically() }
        }

        if (expireJob == null || expireJob?.isCompleted == true) {
            expireJob = scope.launch { expirePeriodically() }
        }
    }

    override suspend fun stop() {
        listenJob?.cancel()
        listenJob = null
        announceJob?.cancel()
        announceJob = null
        expireJob?.cancel()
        expireJob = null

        scope.cancel()
        _peers.value = emptyMap()
    }

    private suspend fun listen() {
        println("Starting UDP peer discovery listener on port $discoveryPort")
        val socket = aSocket(selectorManager).udp().bind(InetSocketAddress("0.0.0.0", discoveryPort))

        try {
            val localDeviceInfo = obtainLocalDeviceInfo()
            while (scope.isActive) {
                val datagram = socket.receive()
                val incomingAddress = datagram.address as? InetSocketAddress ?: continue

                val bytes = datagram.packet.readByteArray()
                val text = bytes.toString(Charsets.UTF_8)
                val announcement = runCatching {
                    json.decodeFromString(UdpPeerAnnouncement.serializer(), text)
                }.getOrNull() ?: continue
                if (announcement.deviceId == localDeviceInfo.id) {
                    continue
                }

                val now = System.currentTimeMillis()
                val peer = announcement.asPeer(type, incomingAddress, now)
                _peers.update { current ->
                    val mutable = current.toMutableMap()
                    mutable[peer.deviceId] = peer
                    mutable.toMap()
                }
            }
        } finally {
            runCatching { socket.close() }
        }
    }

    private suspend fun announcePeriodically() {
        val socket = aSocket(selectorManager).udp().bind(InetSocketAddress("0.0.0.0", 0))
        val broadcast = InetSocketAddress("255.255.255.255", discoveryPort)

        try {
            val localDeviceInfo = obtainLocalDeviceInfo()
            while (scope.isActive) {
                val announcement = UdpPeerAnnouncement(
                    deviceId = localDeviceInfo.id,
                    deviceName = localDeviceInfo.name,
                    deviceType = localDeviceInfo.type,
                    hostEpochMs = hostEpochMs,
                    sessionPort = null,
                    capabilities = Link.Capability.All,
                    supportedLinks = localDeviceInfo.supportedLinks,
                )

                val text = json.encodeToString(UdpPeerAnnouncement.serializer(), announcement)
                val bytes = text.toByteArray(Charsets.UTF_8)
                socket.send(Datagram(ByteReadPacket(bytes), broadcast))

                delay(announceIntervalMs)
            }
        } finally {
            runCatching { socket.close() }
        }
    }

    private suspend fun expirePeriodically() {
        while (scope.isActive) {
            delay(1_000)

            val now = System.currentTimeMillis()
            _peers.update { current ->
                current.filterValues { peer ->
                    (now - peer.lastSeenAtMs) <= ttlMs
                }
            }
        }
    }
}
