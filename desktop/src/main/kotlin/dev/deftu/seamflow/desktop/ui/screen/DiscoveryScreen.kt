package dev.deftu.seamflow.desktop.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.deftu.seamflow.core.DeviceId
import dev.deftu.seamflow.core.protocol.discovery.DiscoveryDriver
import dev.deftu.seamflow.core.protocol.discovery.Peer
import dev.deftu.seamflow.core.protocol.discovery.PeerDiscovery
import dev.deftu.seamflow.core.protocol.link.Link
import dev.deftu.seamflow.desktop.AppState
import dev.deftu.seamflow.desktop.protocol.DesktopDiscoveryDriver
import dev.deftu.seamflow.desktop.protocol.UdpPeerDiscovery
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

@Composable
fun DiscoveryScreen(
    appState: AppState,
    onOpenPeer: (id: DeviceId) -> Unit,
) {
    val discoveryDriver = rememberDiscoveryDriver(
        discoverers = remember {
            setOf(UdpPeerDiscovery(Json, Link.Type.LAN, 10_000L, System.currentTimeMillis()))
        }
    )
    val peers = discoveryDriver.peers.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (peers.value.isEmpty()) {
            Searching()
        } else {
            val combinedPeers = peers.value.values.flatMap { it.entries }.associate { it.toPair() }
            PeerList(
                peers = combinedPeers,
                onOpenPeer = onOpenPeer,
            )
        }
    }
}

@Composable
fun Searching() {
    Text(text = "Searching for peers...")
}

@Composable
fun PeerList(
    peers: Map<DeviceId, Peer>,
    onOpenPeer: (id: DeviceId) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        for ((id, name) in peers) {
            Text(text = "Peer: $name ($id)")
        }
    }
}

@Composable
private fun rememberDiscoveryDriver(
    discoverers: Set<PeerDiscovery>,
): DiscoveryDriver {
    val driver = remember { DesktopDiscoveryDriver() }

    LaunchedEffect(Unit) {
        for (discovery in discoverers) {
            driver.register(discovery.type, discovery)
        }

        driver.start()
    }

    DisposableEffect(Unit) {
        onDispose {
            println("Stopping discovery driver")
            runBlocking { driver.stop() }
        }
    }

    return driver
}
