package com.vibecut.ai.ui.screens.editor

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vibecut.ai.ui.components.editor.*
import com.vibecut.ai.ui.theme.VibeCutColors

@Composable
fun VideoEditorScreen(
    videoUri: String,
    onBack: () -> Unit,
    onUpgrade: () -> Unit,
    viewModel: VideoEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(videoUri) { viewModel.loadVideo(videoUri) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VibeCutColors.Background)
    ) {
        // Top bar
        EditorTopBar(
            onBack = onBack,
            onExport = { viewModel.startExport() },
            onAiMagic = { viewModel.triggerAiMagicEdit() },
            isPremium = uiState.isPremium
        )

        // Video Preview (ExoPlayer)
        VideoPreviewPlayer(
            videoUri = videoUri,
            currentTimeMs = uiState.currentTimeMs,
            isPlaying = uiState.isPlaying,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f)
        )

        // Playback Controls
        PlaybackControls(
            isPlaying = uiState.isPlaying,
            currentTime = uiState.currentTimeMs,
            totalDuration = uiState.totalDurationMs,
            onPlayPause = viewModel::togglePlayPause,
            onSeek = viewModel::seekTo
        )

        // Timeline
        VideoTimeline(
            clips = uiState.clips,
            currentTime = uiState.currentTimeMs,
            onTrimStart = viewModel::trimStart,
            onTrimEnd = viewModel::trimEnd,
            onSplit = viewModel::splitAtCurrentPosition,
            modifier = Modifier.height(80.dp)
        )

        // Tool Tabs
        EditorToolTabs(
            selectedTool = uiState.selectedTool,
            onToolSelected = viewModel::selectTool
        )

        // Tool Panel (changes based on selected tool)
        AnimatedContent(targetState = uiState.selectedTool) { tool ->
            when (tool) {
                EditorTool.TRIM -> TrimToolPanel(viewModel)
                EditorTool.TEXT -> TextToolPanel(viewModel)
                EditorTool.MUSIC -> MusicToolPanel(viewModel)
                EditorTool.FILTERS -> FiltersToolPanel(viewModel)
                EditorTool.EFFECTS -> EffectsToolPanel(viewModel)
                EditorTool.STICKERS -> StickerToolPanel(viewModel)
                EditorTool.AI -> AiToolPanel(viewModel, onUpgrade)
                EditorTool.SPEED -> SpeedToolPanel(viewModel)
            }
        }
    }
}