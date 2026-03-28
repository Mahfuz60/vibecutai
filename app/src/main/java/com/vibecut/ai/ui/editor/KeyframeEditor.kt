package com.vibecut.ai.ui.editor

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

data class Keyframe(
    val timeMs: Long,
    val x: Float = 0.5f,
    val y: Float = 0.5f,
    val scale: Float = 1f,
    val rotation: Float = 0f,
    val alpha: Float = 1f
)

/**
 * Linearly interpolates between two keyframes at a given time.
 */
fun interpolateKeyframe(a: Keyframe, b: Keyframe, timeMs: Long): Keyframe {
    val t = ((timeMs - a.timeMs).toFloat() / (b.timeMs - a.timeMs).toFloat()).coerceIn(0f, 1f)
    return Keyframe(
        timeMs = timeMs,
        x = lerp(a.x, b.x, t),
        y = lerp(a.y, b.y, t),
        scale = lerp(a.scale, b.scale, t),
        rotation = lerp(a.rotation, b.rotation, t),
        alpha = lerp(a.alpha, b.alpha, t)
    )
}

fun lerp(start: Float, stop: Float, fraction: Float): Float = start + fraction * (stop - start)

@Composable
fun KeyframeTimeline(
    keyframes: List<Keyframe>,
    currentTimeMs: Long,
    totalDurationMs: Long,
    onAddKeyframe: (Long) -> Unit,
    onDeleteKeyframe: (Keyframe) -> Unit,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        val width = constraints.maxWidth.toFloat()

        // Timeline track
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Center line
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(0f, size.height / 2f),
                end = Offset(size.width, size.height / 2f),
                strokeWidth = 2f
            )

            // Keyframe dots
            keyframes.forEach { kf ->
                val x = (kf.timeMs.toFloat() / totalDurationMs) * size.width
                drawCircle(
                    color = Color(0xFFFFD700),
                    radius = 8f,
                    center = Offset(x, size.height / 2f)
                )
            }

            // Playhead
            val playheadX = (currentTimeMs.toFloat() / totalDurationMs) * size.width
            drawLine(
                color = Color(0xFFFF4444),
                start = Offset(playheadX, 0f),
                end = Offset(playheadX, size.height),
                strokeWidth = 3f
            )
        }

        // Touch to seek or add keyframe
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(totalDurationMs) {
                    detectTapGestures(
                        onTap = { offset ->
                            val seekMs = ((offset.x / width) * totalDurationMs).toLong()
                            onSeek(seekMs)
                        },
                        onLongPress = { offset ->
                            val timeMs = ((offset.x / width) * totalDurationMs).toLong()
                            onAddKeyframe(timeMs)
                        }
                    )
                }
        )
    }
}

@Composable
fun KeyframePropertyPanel(
    keyframe: Keyframe,
    onUpdate: (Keyframe) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Keyframe at ${keyframe.timeMs}ms", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))

        listOf(
            Triple("Scale", keyframe.scale, 0.1f..4f) to { v: Float -> onUpdate(keyframe.copy(scale = v)) },
            Triple("Rotation", keyframe.rotation, -180f..180f) to { v: Float -> onUpdate(keyframe.copy(rotation = v)) },
            Triple("Alpha", keyframe.alpha, 0f..1f) to { v: Float -> onUpdate(keyframe.copy(alpha = v)) },
        ).forEach { (triple, updater) ->
            val (label, value, range) = triple
            Text(label, style = MaterialTheme.typography.labelMedium)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Slider(value = value, onValueChange = updater, valueRange = range, modifier = Modifier.weight(1f))
                Text("%.2f".format(value), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}