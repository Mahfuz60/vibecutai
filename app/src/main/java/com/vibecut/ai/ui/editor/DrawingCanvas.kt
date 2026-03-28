package com.vibecut.ai.ui.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

data class DrawingPath(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float,
    val isEraser: Boolean = false
)

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    onPathsChanged: (List<DrawingPath>) -> Unit = {}
) {
    val paths = remember { mutableStateListOf<DrawingPath>() }
    val undoneStack = remember { mutableStateListOf<DrawingPath>() }
    var currentPath by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var selectedColor by remember { mutableStateOf(Color.White) }
    var brushSize by remember { mutableFloatStateOf(8f) }
    var isEraser by remember { mutableStateOf(false) }

    val brushColors = listOf(
        Color.White, Color.Black, Color.Red, Color.Yellow,
        Color.Green, Color.Cyan, Color(0xFFFF69B4), Color(0xFFFF6500)
    )

    Column(modifier = modifier) {
        // Drawing surface
        Canvas(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Transparent)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset -> currentPath = listOf(offset) },
                        onDrag = { change, _ ->
                            currentPath = currentPath + change.position
                        },
                        onDragEnd = {
                            if (currentPath.size > 1) {
                                val path = DrawingPath(currentPath, if (isEraser) Color.Transparent else selectedColor, brushSize, isEraser)
                                paths.add(path)
                                undoneStack.clear()
                                onPathsChanged(paths.toList())
                            }
                            currentPath = emptyList()
                        }
                    )
                }
        ) {
            // Draw all completed paths
            paths.forEach { path ->
                if (path.points.size < 2) return@forEach
                val androidPath = Path()
                androidPath.moveTo(path.points.first().x, path.points.first().y)
                path.points.drop(1).forEach { androidPath.lineTo(it.x, it.y) }
                drawPath(
                    path = androidPath,
                    color = if (path.isEraser) Color.Transparent else path.color,
                    style = Stroke(
                        width = path.strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    ),
                    blendMode = if (path.isEraser) BlendMode.Clear else BlendMode.SrcOver
                )
            }

            // Draw active path
            if (currentPath.size > 1) {
                val activePath = Path()
                activePath.moveTo(currentPath.first().x, currentPath.first().y)
                currentPath.drop(1).forEach { activePath.lineTo(it.x, it.y) }
                drawPath(
                    path = activePath,
                    color = if (isEraser) Color.LightGray.copy(alpha = 0.3f) else selectedColor,
                    style = Stroke(width = brushSize, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }
        }

        // Toolbar
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Colors
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                brushColors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(if (selectedColor == color && !isEraser) 32.dp else 26.dp)
                            .background(color, CircleShape)
                            .then(if (selectedColor == color && !isEraser)
                                Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape) else Modifier
                            )
                            .clickable { 
                                selectedColor = color
                                isEraser = false 
                            }
                    )
                }
            }

            // Brush size
            Slider(value = brushSize, onValueChange = { brushSize = it }, valueRange = 3f..40f, modifier = Modifier.width(80.dp))

            // Eraser + Undo + Clear
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconToggleButton(checked = isEraser, onCheckedChange = { isEraser = it }) {
                    Icon(if (isEraser) Icons.Default.EditOff else Icons.Default.Edit, "Eraser")
                }
                IconButton(onClick = { if (paths.isNotEmpty()) { undoneStack.add(paths.removeLast()); onPathsChanged(paths.toList()) } }) {
                    Icon(Icons.AutoMirrored.Filled.Undo, "Undo")
                }
                IconButton(onClick = { paths.clear(); onPathsChanged(emptyList()) }) {
                    Icon(Icons.Default.DeleteForever, "Clear")
                }
            }
        }
    }
}