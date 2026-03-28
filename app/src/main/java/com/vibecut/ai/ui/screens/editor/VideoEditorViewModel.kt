package com.vibecut.ai.ui.screens.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VideoEditorUiState(
    val videoUri: String = "",
    val isPlaying: Boolean = false,
    val currentTimeMs: Long = 0,
    val totalDurationMs: Long = 0,
    val isPremium: Boolean = false,
    val clips: List<String> = emptyList(),
    val selectedTool: EditorTool = EditorTool.TRIM
)

enum class EditorTool {
    TRIM, TEXT, MUSIC, FILTERS, EFFECTS, STICKERS, AI, SPEED
}

@HiltViewModel
class VideoEditorViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(VideoEditorUiState())
    val uiState: StateFlow<VideoEditorUiState> = _uiState.asStateFlow()

    fun loadVideo(uri: String) {
        _uiState.value = _uiState.value.copy(videoUri = uri)
    }

    fun togglePlayPause() {
        _uiState.value = _uiState.value.copy(isPlaying = !_uiState.value.isPlaying)
    }

    fun seekTo(timeMs: Long) {
        _uiState.value = _uiState.value.copy(currentTimeMs = timeMs)
    }

    fun startExport() {}
    fun triggerAiMagicEdit() {}
    fun selectTool(tool: EditorTool) {
        _uiState.value = _uiState.value.copy(selectedTool = tool)
    }
    fun trimStart() {}
    fun trimEnd() {}
    fun splitAtCurrentPosition() {}
}
