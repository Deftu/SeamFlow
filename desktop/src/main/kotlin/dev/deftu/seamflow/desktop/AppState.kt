package dev.deftu.seamflow.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.io.files.Path
import kotlinx.serialization.json.Json

data class AppState(
    val title: String,
    val windowState: WindowState,
) {
    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = true
        prettyPrint = true
    }

    val configPath = Path(System.getenv("SEAMFLOW_CONFIG_PATH") ?: defaultConfigPath())

    val isConnected: StateFlow<Boolean> get() = _isConnected

    private val _isConnected = MutableStateFlow(false)

    fun connect() {
        _isConnected.value = true
    }

    fun disconnect() {
        _isConnected.value = false
    }

    /**
     * %APPDATA%/SeamFlow/seamflow.json on Windows
     * $HOME/.config/SeamFlow/seamflow.json on Linux
     * $HOME/Library/Application Support/SeamFlow/seamflow.json on macOS
     */
    private fun defaultConfigPath(): String {
        val appName = "SeamFlow"
        val configFileName = "seamflow.json"
        val os = System.getProperty("os.name").lowercase()

        val configDir = when {
            os.contains("win") -> System.getenv("APPDATA") ?: "${System.getProperty("user.home")}\\AppData\\Roaming"
            os.contains("mac") -> "${System.getProperty("user.home")}/Library/Application Support"
            else -> System.getenv("XDG_CONFIG_HOME") ?: "${System.getProperty("user.home")}/.config"
        }

        return "$configDir/$appName/$configFileName"
    }
}

@Composable
fun rememberAppState(
    title: String = remember { "SeamFlow" },
    windowState: WindowState = rememberWindowState(
        position = WindowPosition.Aligned(Alignment.Center),
        size = DpSize(1280.dp, 720.dp),
    ),
    isConnected: Boolean = false,
): AppState {
    return remember(title, windowState, isConnected) {
        AppState(
            title = title,
            windowState = windowState,
        )
    }
}
