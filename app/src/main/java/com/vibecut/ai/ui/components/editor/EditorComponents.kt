package com.vibecut.ai.ui.components.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecut.ai.ui.screens.editor.EditorTool
import com.vibecut.ai.ui.screens.editor.VideoEditorViewModel
import com.vibecut.ai.ui.theme.VibeCutColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopBar(
    onBack: () -> Unit,
    onExport: () -> Unit,
    onAiMagic: () -> Unit,
    isPremium: Boolean
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Super Transitions",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = onAiMagic) {
                Icon(Icons.Default.AutoFixHigh, "AI", tint = VibeCutColors.Primary)
            }
            TextButton(onClick = onExport) {
                Text("SAVE", color = VibeCutColors.Secondary, fontWeight = FontWeight.Bold)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun VideoPreviewPlayer(
    videoUri: String,
    currentTimeMs: Long,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.DarkGray)
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        // In a real app, this would be the ExoPlayer surface
        Icon(
            Icons.Default.PlayArrow,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = Color.White.copy(alpha = 0.5f)
        )
        
        // Time overlay
        Text(
            text = "00:04 / 00:15",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
    }
}

@Composable
fun PlaybackControls(
    isPlaying: Boolean,
    currentTime: Long,
    totalDuration: Long,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { /* undo */ }) {
            Icon(Icons.Default.Undo, "Undo", tint = Color.LightGray)
        }
        
        FloatingActionButton(
            onClick = onPlayPause,
            containerColor = Color.White,
            contentColor = Color.Black,
            shape = CircleShape,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, "Play")
        }

        IconButton(onClick = { /* redo */ }) {
            Icon(Icons.Default.Redo, "Redo", tint = Color.LightGray)
        }
    }
}

@Composable
fun VideoTimeline(
    clips: List<String>,
    currentTime: Long,
    onTrimStart: () -> Unit,
    onTrimEnd: () -> Unit,
    onSplit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Timeline tracks
        Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 48.dp)) {
                // Sticker/Text Track
                TimelineTrack(color = VibeCutColors.TimelineTrack3, label = "BOOM!")
                Spacer(modifier = Modifier.height(4.dp))
                // Video Track
                TimelineTrack(color = VibeCutColors.TimelineTrack1, label = "Video Clip 1")
                Spacer(modifier = Modifier.height(4.dp))
                // Audio Track
                TimelineTrack(color = VibeCutColors.TimelineTrack2, label = "Music.mp3")
            }
            
            // Playhead
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(Color.White)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun TimelineTrack(color: Color, label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(24.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(label, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EditorToolTabs(
    selectedTool: EditorTool,
    onToolSelected: (EditorTool) -> Unit
) {
    Surface(
        color = VibeCutColors.Background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ToolItem(Icons.Default.Crop, "CANVAS", selectedTool == EditorTool.AI) { onToolSelected(EditorTool.AI) }
            ToolItem(Icons.Default.ContentCut, "TRIM", selectedTool == EditorTool.TRIM) { onToolSelected(EditorTool.TRIM) }
            ToolItem(Icons.Default.Filter, "FILTER", selectedTool == EditorTool.FILTERS) { onToolSelected(EditorTool.FILTERS) }
            ToolItem(Icons.Default.MusicNote, "MUSIC", selectedTool == EditorTool.MUSIC) { onToolSelected(EditorTool.MUSIC) }
            ToolItem(Icons.Default.Face, "STICKER", selectedTool == EditorTool.STICKERS) { onToolSelected(EditorTool.STICKERS) }
            ToolItem(Icons.Default.Title, "TEXT", selectedTool == EditorTool.TEXT) { onToolSelected(EditorTool.TEXT) }
        }
    }
}

@Composable
fun ToolItem(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (isSelected) VibeCutColors.Primary else Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) VibeCutColors.Primary else Color.Gray,
            fontSize = 8.sp
        )
    }
}

@Composable fun TrimToolPanel(viewModel: VideoEditorViewModel) { 
    Column(modifier = Modifier.padding(16.dp)) {
        Text("BASIC", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(8) { index ->
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(VibeCutColors.SurfaceVariant)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable fun TextToolPanel(viewModel: VideoEditorViewModel) { Text("Text Tools", modifier = Modifier.padding(16.dp), color = Color.White) }
@Composable fun MusicToolPanel(viewModel: VideoEditorViewModel) { Text("Audio Tracks", modifier = Modifier.padding(16.dp), color = Color.White) }
@Composable fun FiltersToolPanel(viewModel: VideoEditorViewModel) { Text("Filters", modifier = Modifier.padding(16.dp), color = Color.White) }
@Composable fun EffectsToolPanel(viewModel: VideoEditorViewModel) { Text("Effects", modifier = Modifier.padding(16.dp), color = Color.White) }
@Composable fun StickerToolPanel(viewModel: VideoEditorViewModel) { Text("Stickers", modifier = Modifier.padding(16.dp), color = Color.White) }
@Composable fun AiToolPanel(viewModel: VideoEditorViewModel, onUpgrade: () -> Unit) { Text("AI Magic", modifier = Modifier.padding(16.dp), color = Color.White) }
@Composable fun SpeedToolPanel(viewModel: VideoEditorViewModel) { Text("Speed Control", modifier = Modifier.padding(16.dp), color = Color.White) }
