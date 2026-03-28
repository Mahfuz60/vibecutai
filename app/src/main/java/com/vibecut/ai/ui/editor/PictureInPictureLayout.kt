package com.vibecut.ai.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

enum class PipPosition { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER }

@Composable
fun PictureInPictureLayout(
    mainContent: @Composable () -> Unit,
    pipContent: @Composable () -> Unit,
    pipPosition: PipPosition = PipPosition.BOTTOM_RIGHT,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val containerWidth = constraints.maxWidth.toFloat()
        val containerHeight = constraints.maxHeight.toFloat()

        // Default PiP size: 30% of container width, aspect ratio 9:16
        val pipWidth = containerWidth * 0.30f
        val pipHeight = pipWidth * (16f / 9f)

        var offsetX by remember {
            mutableFloatStateOf(
                when (pipPosition) {
                    PipPosition.TOP_LEFT, PipPosition.BOTTOM_LEFT -> 16f
                    PipPosition.TOP_RIGHT, PipPosition.BOTTOM_RIGHT -> containerWidth - pipWidth - 16f
                    PipPosition.CENTER -> (containerWidth - pipWidth) / 2f
                }
            )
        }
        var offsetY by remember {
            mutableFloatStateOf(
                when (pipPosition) {
                    PipPosition.TOP_LEFT, PipPosition.TOP_RIGHT -> 16f
                    PipPosition.CENTER -> (containerHeight - pipHeight) / 2f
                    else -> containerHeight - pipHeight - 16f
                }
            )
        }
        var scale by remember { mutableFloatStateOf(1f) }

        // Main video (full screen)
        mainContent()

        // PiP overlay
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(
                    width = with(density) { (pipWidth * scale).toDp() },
                    height = with(density) { (pipHeight * scale).toDp() }
                )
                .clip(RoundedCornerShape(12.dp))
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        offsetX = (offsetX + pan.x).coerceIn(0f, containerWidth - pipWidth * scale)
                        offsetY = (offsetY + pan.y).coerceIn(0f, containerHeight - pipHeight * scale)
                        scale = (scale * zoom).coerceIn(0.5f, 2f)
                    }
                }
        ) {
            pipContent()
        }
    }
}

@Composable
fun PipControls(
    currentPosition: PipPosition,
    onPositionChange: (PipPosition) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PipPosition.entries.forEach { pos ->
            val label = when (pos) {
                PipPosition.TOP_LEFT -> "↖"
                PipPosition.TOP_RIGHT -> "↗"
                PipPosition.BOTTOM_LEFT -> "↙"
                PipPosition.BOTTOM_RIGHT -> "↘"
                PipPosition.CENTER -> "+"
            }
            androidx.compose.material3.FilterChip(
                selected = currentPosition == pos,
                onClick = { onPositionChange(pos) },
                label = { androidx.compose.material3.Text(label) }
            )
        }
    }
}