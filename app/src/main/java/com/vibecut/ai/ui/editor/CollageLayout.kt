package com.vibecut.ai.ui.editor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

enum class CollageLayoutType(val label: String, val rows: Int, val cols: Int) {
    SINGLE("Full", 1, 1),
    SPLIT_H("H-Split", 1, 2),
    SPLIT_V("V-Split", 2, 1),
    GRID_2X2("2x2 Grid", 2, 2),
    GRID_3X1("3-Row", 3, 1),
    GRID_1X3("3-Col", 1, 3),
    GRID_3X2("3x2", 3, 2),
    GRID_2X3("2x3", 2, 3),
    TRIPTYCH("Triptych", 1, 3),
    QUAD_LARGE("Quad+Big", 2, 2),
    MOSAIC("Mosaic", 3, 3),
    FILM_STRIP("Film Strip", 1, 4),
}

@Immutable
data class CollageCellContent(val uri: String, val isVideo: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollageEditor(
    selectedLayout: CollageLayoutType,
    cells: List<CollageCellContent?>,
    onCellTap: (cellIndex: Int) -> Unit,
    onLayoutChanged: (CollageLayoutType) -> Unit,
    borderColor: Color = Color.White,
    borderWidth: Float = 4f
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Layout picker
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CollageLayoutType.entries.forEach { layout ->
                FilterChip(
                    selected = layout == selectedLayout,
                    onClick = { onLayoutChanged(layout) },
                    label = { Text(layout.label, style = MaterialTheme.typography.labelSmall) }
                )
            }
        }

        // Collage canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.Black)
                .padding(borderWidth.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(borderWidth.dp)
            ) {
                repeat(selectedLayout.rows) { row ->
                    Row(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(borderWidth.dp)
                    ) {
                        repeat(selectedLayout.cols) { col ->
                            val cellIndex = row * selectedLayout.cols + col
                            val content = cells.getOrNull(cellIndex)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(Color.DarkGray)
                                    .clickable { onCellTap(cellIndex) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (content != null) {
                                    AsyncImage(
                                        model = content.uri,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    if (content.isVideo) {
                                        Icon(
                                            imageVector = Icons.Default.PlayCircle,
                                            contentDescription = "Video",
                                            tint = Color.White.copy(alpha = 0.7f),
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                } else {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.AddCircleOutline,
                                            contentDescription = "Add",
                                            tint = Color.White.copy(alpha = 0.5f),
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Text(
                                            "Tap to add",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
