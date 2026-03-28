package com.vibecut.ai.ui.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class TextOverlay(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String = "Add Text",
    val color: Color = Color.White,
    val fontSize: Float = 32f,
    val fontWeight: FontWeight = FontWeight.Bold,
    val background: Color = Color.Transparent,
    var x: Float = 0.5f,  // 0..1 normalized
    var y: Float = 0.5f,
    var scale: Float = 1f,
    var rotation: Float = 0f,
    val startMs: Long = 0,
    val endMs: Long = Long.MAX_VALUE
)

@Composable
fun DraggableTextOverlay(
    overlay: TextOverlay,
    containerWidth: Float,
    containerHeight: Float,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onUpdate: (TextOverlay) -> Unit,
    onDelete: () -> Unit
) {
    var posX by remember { mutableFloatStateOf(overlay.x * containerWidth) }
    var posY by remember { mutableFloatStateOf(overlay.y * containerHeight) }
    var scale by remember { mutableFloatStateOf(overlay.scale) }
    var rotation by remember { mutableFloatStateOf(overlay.rotation) }

    Box(
        modifier = Modifier
            .offset { IntOffset(posX.roundToInt(), posY.roundToInt()) }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, rotationDelta ->
                    posX = (posX + pan.x).coerceIn(0f, containerWidth)
                    posY = (posY + pan.y).coerceIn(0f, containerHeight)
                    scale = (scale * zoom).coerceIn(0.5f, 4f)
                    rotation += rotationDelta
                    onUpdate(overlay.copy(
                        x = posX / containerWidth,
                        y = posY / containerHeight,
                        scale = scale,
                        rotation = rotation
                    ))
                }
            }
            .clickable { onSelect() }
    ) {
        Text(
            text = overlay.text,
            style = TextStyle(
                color = overlay.color,
                fontSize = (overlay.fontSize * scale).sp,
                fontWeight = overlay.fontWeight,
                textAlign = TextAlign.Center,
                background = overlay.background
            ),
            modifier = Modifier
                .rotate(rotation)
                .padding(8.dp)
        )

        // Selection handles
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .border(2.dp, MaterialTheme.colorScheme.primary)
            )
            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
            ) {
                Icon(Icons.Default.Close, "Delete", tint = Color.Red, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun TextStylePanel(
    overlay: TextOverlay,
    onUpdate: (TextOverlay) -> Unit
) {
    val fontColors = listOf(Color.White, Color.Black, Color.Yellow, Color(0xFFFF69B4), Color.Cyan, Color.Green)
    val fontSizes = listOf(20f, 28f, 36f, 48f, 64f, 80f)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Font Size", style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            fontSizes.forEach { size ->
                FilterChip(
                    selected = overlay.fontSize == size,
                    onClick = { onUpdate(overlay.copy(fontSize = size)) },
                    label = { Text(size.toInt().toString()) }
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("Color", style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            fontColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color, androidx.compose.foundation.shape.CircleShape)
                        .border(
                            if (overlay.color == color) 3.dp else 1.dp,
                            if (overlay.color == color) MaterialTheme.colorScheme.primary else Color.Gray,
                            androidx.compose.foundation.shape.CircleShape
                        )
                        .clickable { onUpdate(overlay.copy(color = color)) }
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(FontWeight.Normal, FontWeight.Bold, FontWeight.Black).forEach { weight ->
                val label = when (weight) {
                    FontWeight.Bold -> "Bold"
                    FontWeight.Black -> "Heavy"
                    else -> "Normal"
                }
                FilterChip(
                    selected = overlay.fontWeight == weight,
                    onClick = { onUpdate(overlay.copy(fontWeight = weight)) },
                    label = { Text(label) }
                )
            }
        }
    }
}