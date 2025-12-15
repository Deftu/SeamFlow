package dev.deftu.seamflow.desktop.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import dev.deftu.seamflow.desktop.AppState
import kotlinx.coroutines.delay
import java.awt.Frame
import java.awt.event.WindowEvent

@Composable
fun FrameWindowScope.PrimaryContainer(appState: AppState) {
    val config by appState.config.collectAsState()
    val isSystemDark = isSystemInDarkTheme()
    val isConfigDark = config?.isDarkTheme
    val isDark = isConfigDark ?: isSystemDark
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        appState.readConfig()
        delay(5_000L)
        isLoading = false
    }

    MaterialTheme(
        if (isDark) DefignColors.Dark else DefignColors.Light
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colors.background,
        ) {
            Column(Modifier.fillMaxSize()) {
                when (isLoading || config == null) {
                    true -> SplashScreen()

                    false -> {
                        TitleBar(
                            title = appState.title,
                            onMinimize = window::minimize,
                            onClose = window::close
                        )

                        DesktopUI(appState)
                    }
                }
            }
        }
    }
}

private fun ComposeWindow.minimize() {
    extendedState = extendedState or Frame.ICONIFIED
}

private fun ComposeWindow.close() {
    dispatchEvent(WindowEvent(this, WindowEvent.WINDOW_CLOSING))
}
