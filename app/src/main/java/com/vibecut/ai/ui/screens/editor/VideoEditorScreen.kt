package com.vibecut.ai.ui.screens.editor

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vibecut.ai.ui.components.editor.EditorToolTabs
import com.vibecut.ai.ui.components.editor.EditorTopBar
import com.vibecut.ai.ui.components.editor.EffectsToolPanel
import com.vibecut.ai.ui.components.editor.ExportToolPanel
import com.vibecut.ai.ui.components.editor.FiltersToolPanel
import com.vibecut.ai.ui.components.editor.MusicToolPanel
import com.vibecut.ai.ui.components.editor.PlaybackControls
import com.vibecut.ai.ui.components.editor.PreviewQualityStrip
import com.vibecut.ai.ui.components.editor.ProjectToolPanel
import com.vibecut.ai.ui.components.editor.StickerToolPanel
import com.vibecut.ai.ui.components.editor.TextToolPanel
import com.vibecut.ai.ui.components.editor.TimelineToolPanel
import com.vibecut.ai.ui.components.editor.TransformToolPanel
import com.vibecut.ai.ui.components.editor.TrimToolPanel
import com.vibecut.ai.ui.components.editor.VideoPreviewPlayer
import com.vibecut.ai.ui.components.editor.VideoTimeline
import com.vibecut.ai.ui.theme.VibeCutColors

@Composable
fun VideoEditorScreen(
    videoUri: String,
    onBack: () -> Unit,
    onUpgrade: () -> Unit,
    viewModel: VideoEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(videoUri) { viewModel.loadVideo(videoUri) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VibeCutColors.Background)
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyUp) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.Spacebar -> {
                        viewModel.togglePlayPause(); true
                    }
                    Key.DirectionLeft -> {
                        viewModel.frameStep(false); true
                    }
                    Key.DirectionRight -> {
                        viewModel.frameStep(true); true
                    }
                    else -> false
                }
            }
    ) {
        EditorTopBar(
            onBack = onBack,
            onExport = { viewModel.startExport() },
            onAiMagic = { viewModel.triggerAiMagicEdit() },
            onDonate = { uriHandler.openUri(uiState.donationUrl) },
            isPremium = uiState.isPremium
        )

        VideoPreviewPlayer(
            uiState = uiState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f)
        )

        PreviewQualityStrip(
            current = uiState.previewQuality,
            onSelect = viewModel::setPreviewQuality
        )

        PlaybackControls(
            uiState = uiState,
            onPlayPause = viewModel::togglePlayPause,
            onSeek = viewModel::seekTo,
            onFrameStep = viewModel::frameStep,
            onToggleLoop = viewModel::toggleLoopPlayback,
            onUndo = viewModel::undo,
            onRedo = viewModel::redo
        )

        VideoTimeline(
            uiState = uiState,
            onSelectClip = viewModel::selectClip,
            onSplit = viewModel::splitAtCurrentPosition,
            onTrimStart = viewModel::trimStart,
            onTrimEnd = viewModel::trimEnd,
            onDelete = viewModel::deleteSelectedClip,
            onReorder = viewModel::reorderClip,
            onZoomChange = viewModel::setTimelineZoom,
            modifier = Modifier.height(220.dp)
        )

        EditorToolTabs(
            selectedTool = uiState.selectedTool,
            onToolSelected = viewModel::selectTool
        )

        AnimatedContent(targetState = uiState.selectedTool, label = "tool-panel") { tool ->
            when (tool) {
                EditorTool.TIMELINE -> TimelineToolPanel(viewModel)
                EditorTool.TRIM -> TrimToolPanel(viewModel)
                EditorTool.TEXT -> TextToolPanel(viewModel)
                EditorTool.MUSIC -> MusicToolPanel(viewModel)
                EditorTool.FILTERS -> FiltersToolPanel(viewModel)
                EditorTool.EFFECTS -> EffectsToolPanel(viewModel)
                EditorTool.STICKERS -> StickerToolPanel(viewModel)
                EditorTool.TRANSFORM -> TransformToolPanel(viewModel)
                EditorTool.EXPORT -> ExportToolPanel(viewModel)
                EditorTool.PROJECT -> ProjectToolPanel(viewModel)
            }
        }

        Surface(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), color = VibeCutColors.SurfaceVariant) {
            LazyColumn(modifier = Modifier.padding(12.dp)) {
                item { Text("Quick Frontend Feature Checklist", color = Color.White) }
                items(
                    listOf(
                        "Upload/import local files UI ready",
                        "Single/multi-track timeline controls",
                        "Cut/split/trim/delete/reorder",
                        "Zoom scrubber and frame stepping",
                        "Preview quality + loop playback",
                        "Transform + opacity + rotation",
                        "Filters: grayscale / brightness / contrast",
                        "Text overlays + animation chips",
                        "Audio volume, mute, speed",
                        "Export resolution + quality controls",
                        "Project JSON save/restore",
                        "Undo/redo keyboard + toolbar controls"
                    )
                ) { item ->
                    Text("• $item", color = Color.LightGray, modifier = Modifier.padding(vertical = 1.dp))
                }
            }
        }
    }
}