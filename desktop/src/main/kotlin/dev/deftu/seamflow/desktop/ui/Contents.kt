package dev.deftu.seamflow.desktop.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import dev.deftu.seamflow.desktop.rememberAppState
import java.awt.Color
import java.awt.geom.RoundRectangle2D

@Composable
fun ApplicationScope.Contents() {
    val state = rememberAppState()

    Window(
        onCloseRequest = ::exitApplication,
        state = state.windowState,
        title = state.title,
        undecorated = true,
        transparent = true,
        resizable = false,
    ) {
        RoundedWindow()
        PrimaryContainer(state)
    }
}

@Composable
private fun WindowScope.RoundedWindow() {
    SideEffect {
        window.background = Color(0, 0, 0, 0)
    }

    val radius = 8f
    DisposableEffect(window.width, window.height) {
        window.shape = RoundRectangle2D.Float(0f, 0f, window.width.toFloat(), window.height.toFloat(), radius * 2f, radius * 2f)

        onDispose {
            window.shape = null
        }
    }
}
