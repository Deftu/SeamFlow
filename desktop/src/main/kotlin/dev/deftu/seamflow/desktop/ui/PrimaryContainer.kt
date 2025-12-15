package dev.deftu.seamflow.desktop.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import dev.deftu.seamflow.desktop.AppState
import dev.deftu.seamflow.desktop.config.AppConfig
import kotlinx.coroutines.launch
import java.awt.Frame
import java.awt.event.WindowEvent

@Composable
fun FrameWindowScope.PrimaryContainer(appState: AppState) {
    val scope = rememberCoroutineScope()
    val isSystemDark = isSystemInDarkTheme()
    var isLoading by remember { mutableStateOf(true) }
    var config by remember { mutableStateOf<AppConfig?>(null) }
    val isDark = config?.isDarkTheme ?: isSystemDark

    LaunchedEffect(Unit) {
        scope.launch {
            config = AppConfig.readFrom(appState.json, appState.configPath)
            isLoading = false
        }
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
                when (isLoading && config == null) {
                    true -> SplashScreen()

                    false -> {
                        TitleBar(
                            title = appState.title,
                            onMinimize = window::minimize,
                            onClose = window::close
                        )

                        DesktopUI(appState, config!!)
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
