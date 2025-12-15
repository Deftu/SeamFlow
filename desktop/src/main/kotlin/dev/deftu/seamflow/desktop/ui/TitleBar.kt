@file:OptIn(ExperimentalComposeUiApi::class)

package dev.deftu.seamflow.desktop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import dev.deftu.seamflow.desktop.utils.noRippleClickable

@Composable
fun WindowScope.TitleBar(
    title: String,
    onMinimize: () -> Unit,
    onClose: () -> Unit,
) {
    val colors = MaterialTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colors.surface)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WindowDraggableArea(Modifier.weight(1f).fillMaxHeight()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(text = title, style = MaterialTheme.typography.h6, color = colors.onSurface)
            }
        }

        Row(Modifier.padding(end = 6.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            WindowControlButton(
                label = "–",
                onClick = onMinimize,
                contentColor = colors.onSurface,
                hoverColor = colors.onSurface.copy(alpha = 0.12f).compositeOver(colors.surface),
                activeColor = colors.onSurface.copy(alpha = 0.20f).compositeOver(colors.surface),
            )

            WindowControlButton(
                label = "×",
                onClick = onClose,
                contentColor = colors.onSurface,
                hoverColor = colors.error.copy(alpha = 0.90f),
                activeColor = colors.error.copy(alpha = 0.75f),
            )
        }
    }
}

@Composable
private fun WindowControlButton(
    label: String,
    onClick: () -> Unit,
    hoverColor: Color,
    activeColor: Color = hoverColor,
    contentColor: Color = Color.White,
) {
    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val backgroundColor = when {
        isPressed -> activeColor
        isHovered -> hoverColor
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .size(width = 40.dp, height = 32.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .pointerMoveFilter(
                onEnter = { isHovered = true; false },
                onExit = { isHovered = false; false },
            ).noRippleClickable {
                isPressed = true
                try {
                    onClick()
                } finally {
                    isPressed = false
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, color = contentColor, fontWeight = FontWeight.Medium)
    }
}
