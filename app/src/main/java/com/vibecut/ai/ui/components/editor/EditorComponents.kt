@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.vibecut.ai.ui.components.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.GifBox
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.MotionPhotosAuto
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Transform
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecut.ai.ui.screens.editor.EditorFilter
import com.vibecut.ai.ui.screens.editor.EditorTool
import com.vibecut.ai.ui.screens.editor.ExportResolution
import com.vibecut.ai.ui.screens.editor.PreviewQuality
import com.vibecut.ai.ui.screens.editor.TextAnimation
import com.vibecut.ai.ui.screens.editor.TimelineClip
import com.vibecut.ai.ui.screens.editor.TransitionType
import com.vibecut.ai.ui.screens.editor.VideoEditorUiState
import com.vibecut.ai.ui.screens.editor.VideoEditorViewModel
import com.vibecut.ai.ui.theme.VibeCutColors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditorTopBar(
    onBack: () -> Unit,
    onExport: () -> Unit,
    onAiMagic: () -> Unit,
    onDonate: () -> Unit,
    isPremium: Boolean
) {
    CenterAlignedTopAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "VibeCut Studio Pro",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    if (isPremium) "Professional Workspace" else "Creator Workspace",
                    style = MaterialTheme.typography.labelSmall,
                    color = VibeCutColors.Secondary
                )
            }
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
            IconButton(onClick = onDonate) {
                Icon(Icons.Default.VolunteerActivism, "Donate", tint = VibeCutColors.Secondary)
            }
            TextButton(onClick = onExport) {
                Text("EXPORT", color = VibeCutColors.Secondary, fontWeight = FontWeight.Bold)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun VideoPreviewPlayer(
    uiState: VideoEditorUiState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161616))
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        when (uiState.selectedFilter) {
                            EditorFilter.NONE -> Color(0xFF111111)
                            EditorFilter.GRAYSCALE -> Color(0xFF707070)
                            EditorFilter.BRIGHTNESS_UP -> Color(0xFF2E2E2E)
                            EditorFilter.CONTRAST_UP -> Color(0xFF090909)
                        }
                    )
                    .alpha(uiState.opacity),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(52.dp),
                    tint = Color.White.copy(alpha = 0.85f)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    InfoPill("Quality: ${uiState.previewQuality.label}")
                    Spacer(modifier = Modifier.height(6.dp))
                    InfoPill("Filter: ${uiState.selectedFilter.name}")
                }
                uiState.textLayers.take(1).firstOrNull()?.let { textLayer ->
                    Text(
                        text = textLayer.text,
                        color = Color(textLayer.colorHex),
                        fontSize = textLayer.fontSize.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(start = (textLayer.posX * 40).dp, top = (textLayer.posY * 40).dp)
                    )
                }
            }

            Text(
                text = "${formatTime(uiState.currentTimeMs)} / ${formatTime(uiState.totalDurationMs)}",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
    }
}

@Composable
private fun InfoPill(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.Black.copy(alpha = 0.55f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = label, color = Color.White, fontSize = 10.sp)
    }
}

@Composable
fun PlaybackControls(
    uiState: VideoEditorUiState,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onFrameStep: (Boolean) -> Unit,
    onToggleLoop: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onUndo, enabled = uiState.canUndo) {
                    Icon(Icons.Default.Undo, "Undo", tint = if (uiState.canUndo) Color.White else Color.Gray)
                }
                IconButton(onClick = onRedo, enabled = uiState.canRedo) {
                    Icon(Icons.Default.Redo, "Redo", tint = if (uiState.canRedo) Color.White else Color.Gray)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onFrameStep(false) }) {
                    Icon(Icons.Default.FastForward, "Prev frame", tint = Color.LightGray, modifier = Modifier.size(18.dp))
                }
                FloatingActionButton(
                    onClick = onPlayPause,
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    shape = CircleShape,
                    modifier = Modifier.size(46.dp)
                ) {
                    Icon(if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, "Play")
                }
                IconButton(onClick = { onFrameStep(true) }) {
                    Icon(Icons.Default.FastForward, "Next frame", tint = Color.LightGray)
                }
            }

            IconButton(onClick = onToggleLoop) {
                Icon(
                    Icons.Default.Loop,
                    "Loop",
                    tint = if (uiState.loopPlayback) VibeCutColors.Secondary else Color.Gray
                )
            }
        }

        Slider(
            value = uiState.currentTimeMs.toFloat(),
            onValueChange = { onSeek(it.toLong()) },
            valueRange = 0f..uiState.totalDurationMs.coerceAtLeast(1).toFloat(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun VideoTimeline(
    uiState: VideoEditorUiState,
    onSelectClip: (String) -> Unit,
    onSplit: () -> Unit,
    onTrimStart: () -> Unit,
    onTrimEnd: () -> Unit,
    onDelete: () -> Unit,
    onReorder: (Int, Int) -> Unit,
    onZoomChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(VibeCutColors.SurfaceVariant)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Timeline", color = Color.White, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Zoom", color = Color.LightGray, fontSize = 12.sp)
                Slider(
                    value = uiState.timelineZoom,
                    onValueChange = onZoomChange,
                    valueRange = 0.5f..3f,
                    modifier = Modifier.width(120.dp)
                )
            }
        }

        val scrollState = rememberScrollState()
        Column(modifier = Modifier.horizontalScroll(scrollState)) {
            listOf(0, 1).forEach { trackIndex ->
                TrackRow(
                    trackIndex = trackIndex,
                    clips = uiState.clips.filter { it.track == trackIndex },
                    selectedClipId = uiState.selectedClipId,
                    zoom = uiState.timelineZoom,
                    onSelectClip = onSelectClip,
                    onReorder = onReorder,
                    allClips = uiState.clips
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MiniAction("Split", Icons.Default.ContentCut, onSplit)
            MiniAction("Trim In", Icons.Default.ContentCut, onTrimStart)
            MiniAction("Trim Out", Icons.Default.ContentCut, onTrimEnd)
            MiniAction("Delete", Icons.Default.Delete, onDelete)
        }
    }
}

@Composable
private fun TrackRow(
    trackIndex: Int,
    clips: List<TimelineClip>,
    selectedClipId: String?,
    zoom: Float,
    onSelectClip: (String) -> Unit,
    onReorder: (Int, Int) -> Unit,
    allClips: List<TimelineClip>
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("T${trackIndex + 1}", color = Color.LightGray, modifier = Modifier.width(30.dp), fontSize = 11.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            clips.forEach { clip ->
                val idx = allClips.indexOfFirst { it.id == clip.id }
                Card(
                    modifier = Modifier
                        .width((clip.durationMs / 130f * zoom).dp.coerceAtLeast(60.dp))
                        .height(36.dp)
                        .clickable { onSelectClip(clip.id) }
                        .border(
                            width = if (selectedClipId == clip.id) 2.dp else 0.dp,
                            color = if (selectedClipId == clip.id) VibeCutColors.Secondary else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color(clip.colorHex))
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            clip.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White,
                            fontSize = 10.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Row {
                            Text("◀", color = Color.White, modifier = Modifier.clickable(enabled = idx > 0) { onReorder(idx, idx - 1) })
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("▶", color = Color.White, modifier = Modifier.clickable(enabled = idx < allClips.lastIndex) { onReorder(idx, idx + 1) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniAction(label: String, icon: ImageVector, onClick: () -> Unit) {
    AssistChip(onClick = onClick, label = { Text(label) }, leadingIcon = { Icon(icon, null) })
}

@Composable
fun EditorToolTabs(selectedTool: EditorTool, onToolSelected: (EditorTool) -> Unit) {
    val tools = listOf(
        EditorTool.TIMELINE to Pair(Icons.Default.GridOn, "Timeline"),
        EditorTool.TRIM to Pair(Icons.Default.ContentCut, "Cut"),
        EditorTool.TRANSFORM to Pair(Icons.Default.Transform, "Transform"),
        EditorTool.FILTERS to Pair(Icons.Default.Filter, "Filter"),
        EditorTool.TEXT to Pair(Icons.Default.TextFields, "Text"),
        EditorTool.MUSIC to Pair(Icons.Default.MusicNote, "Audio"),
        EditorTool.STICKERS to Pair(Icons.Default.Image, "Overlay"),
        EditorTool.EFFECTS to Pair(Icons.Default.MotionPhotosAuto, "Transition"),
        EditorTool.EXPORT to Pair(Icons.Default.HighQuality, "Export"),
        EditorTool.PROJECT to Pair(Icons.Default.Save, "Project")
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tools.forEach { (tool, meta) ->
            val selected = tool == selectedTool
            AssistChip(
                onClick = { onToolSelected(tool) },
                label = { Text(meta.second) },
                leadingIcon = { Icon(meta.first, null) },
                colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
                    containerColor = if (selected) VibeCutColors.Primary.copy(alpha = 0.28f) else VibeCutColors.SurfaceVariant,
                    labelColor = if (selected) Color.White else Color.LightGray,
                    leadingIconContentColor = if (selected) Color.White else Color.LightGray
                )
            )
        }
    }
}

@Composable
fun TimelineToolPanel(viewModel: VideoEditorViewModel) {
    val state by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Import & Timeline", color = Color.White, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {}) { Icon(Icons.Default.UploadFile, null); Spacer(Modifier.width(4.dp)); Text("Import Media") }
            OutlinedButton(onClick = {}) { Icon(Icons.Default.Add, null); Spacer(Modifier.width(4.dp)); Text("Add Track") }
        }
        Text("Clips: ${state.clips.size} • Selected: ${state.selectedClipId ?: "None"}", color = Color.LightGray)
    }
}

@Composable
fun TrimToolPanel(viewModel: VideoEditorViewModel) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Cut / Trim / Reorder", color = Color.White, fontWeight = FontWeight.Bold)
        Text("Use timeline actions to split, trim start/end, delete and reorder clips.", color = Color.LightGray)
    }
}

@Composable
fun TransformToolPanel(viewModel: VideoEditorViewModel) {
    val state by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Transform", color = Color.White, fontWeight = FontWeight.Bold)
        LabeledSlider("Scale", state.scale, 0.3f..2.5f) { viewModel.setScale(it) }
        LabeledSlider("Rotation", state.rotation, -180f..180f) { viewModel.setRotation(it) }
        LabeledSlider("Opacity", state.opacity, 0f..1f) { viewModel.setOpacity(it) }
        LabeledSlider("Position X", state.positionX, -1f..1f) { viewModel.setPosition(it, state.positionY) }
        LabeledSlider("Position Y", state.positionY, -1f..1f) { viewModel.setPosition(state.positionX, it) }
    }
}

@Composable
fun TextToolPanel(viewModel: VideoEditorViewModel) {
    val state by viewModel.uiState.collectAsState()
    var draftText by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Text & Overlays", color = Color.White, fontWeight = FontWeight.Bold)
        androidx.compose.material3.OutlinedTextField(
            value = draftText,
            onValueChange = { draftText = it },
            label = { Text("Overlay text") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.addTextLayer(draftText); draftText = "" }) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(4.dp))
                Text("Add Text")
            }
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextAnimation.entries.forEach { animation ->
                AssistChip(
                    onClick = { viewModel.updateTextStyle(animation = animation) },
                    label = { Text(animation.name) },
                    leadingIcon = { Icon(Icons.Default.GifBox, null) }
                )
            }
        }
        Text("Layers: ${state.textLayers.size}", color = Color.LightGray)
    }
}

@Composable
fun MusicToolPanel(viewModel: VideoEditorViewModel) {
    val state by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Audio", color = Color.White, fontWeight = FontWeight.Bold)
        LabeledSlider("Volume", state.volume, 0f..1f) { viewModel.setVolume(it) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = viewModel::toggleMute) {
                Icon(if (state.muted) Icons.Default.VolumeOff else Icons.Default.VolumeUp, null)
                Spacer(Modifier.width(4.dp))
                Text(if (state.muted) "Unmute" else "Mute")
            }
            OutlinedButton(onClick = {}) { Icon(Icons.Default.GraphicEq, null); Spacer(Modifier.width(4.dp)); Text("Waveform") }
        }
        LabeledSlider("Speed", state.speed, 0.25f..3f) { viewModel.setSpeed(it) }
    }
}

@Composable
fun FiltersToolPanel(viewModel: VideoEditorViewModel) {
    val state by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Basic Filters", color = Color.White, fontWeight = FontWeight.Bold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            EditorFilter.entries.forEach { filter ->
                AssistChip(
                    onClick = { viewModel.setFilter(filter) },
                    label = { Text(filter.name) },
                    leadingIcon = { Icon(Icons.Default.Filter, null) }
                )
            }
        }
        Text("Selected: ${state.selectedFilter.name}", color = Color.LightGray)
    }
}

@Composable
fun EffectsToolPanel(viewModel: VideoEditorViewModel) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Transitions", color = Color.White, fontWeight = FontWeight.Bold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TransitionType.entries.forEach { transition ->
                AssistChip(
                    onClick = { viewModel.setTransition(transition) },
                    label = { Text(transition.name) },
                    leadingIcon = { Icon(Icons.Default.MotionPhotosAuto, null) }
                )
            }
        }
    }
}

@Composable
fun StickerToolPanel(viewModel: VideoEditorViewModel) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Stickers & Image Overlays", color = Color.White, fontWeight = FontWeight.Bold)
        Text("Drop PNG/JPG assets and position them as overlays.", color = Color.LightGray)
    }
}

@Composable
fun ExportToolPanel(viewModel: VideoEditorViewModel) {
    val state by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Export", color = Color.White, fontWeight = FontWeight.Bold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ExportResolution.entries.forEach { res ->
                AssistChip(
                    onClick = { viewModel.setExportResolution(res) },
                    label = { Text(res.label) },
                    leadingIcon = { Icon(Icons.Default.HighQuality, null) }
                )
            }
        }
        LabeledSlider("Quality", state.exportQuality.toFloat(), 35f..100f) { viewModel.setExportQuality(it.toInt()) }
        Text("Pipeline ready for MediaRecorder / FFmpeg WASM integration.", color = Color.LightGray)
    }
}

@Composable
fun ProjectToolPanel(viewModel: VideoEditorViewModel) {
    val state by viewModel.uiState.collectAsState()
    var jsonInput by remember(state.projectJson) { mutableStateOf(state.projectJson) }
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Project Save / Restore", color = Color.White, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { viewModel.exportProjectJson() }) { Text("Save JSON") }
            OutlinedButton(onClick = { viewModel.importProjectJson(jsonInput) }) { Text("Load JSON") }
        }
        androidx.compose.material3.OutlinedTextField(
            value = jsonInput,
            onValueChange = { jsonInput = it },
            label = { Text("Project JSON") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PreviewQualityStrip(current: PreviewQuality, onSelect: (PreviewQuality) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PreviewQuality.entries.forEach { quality ->
            AssistChip(
                onClick = { onSelect(quality) },
                label = { Text(quality.label) },
                leadingIcon = { Icon(Icons.Default.HighQuality, null) },
                colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
                    containerColor = if (current == quality) VibeCutColors.Primary.copy(alpha = 0.3f) else VibeCutColors.SurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun LabeledSlider(label: String, value: Float, range: ClosedFloatingPointRange<Float>, onChange: (Float) -> Unit) {
    Column {
        Text("$label: ${"%.2f".format(value)}", color = Color.LightGray, fontSize = 12.sp)
        Slider(value = value, onValueChange = onChange, valueRange = range)
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}