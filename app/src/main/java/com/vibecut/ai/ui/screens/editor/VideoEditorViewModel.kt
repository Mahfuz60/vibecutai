package com.vibecut.ai.ui.screens.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.vibecut.ai.ui.theme.VibeCutColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

data class TimelineClip(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val track: Int,
    val startMs: Long,
    val durationMs: Long,
    val colorHex: Long
)

data class TextLayer(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val colorHex: Long,
    val fontSize: Float,
    val posX: Float,
    val posY: Float,
    val animation: TextAnimation
)

enum class TextAnimation { NONE, FADE_IN, FADE_OUT, SLIDE_IN }

enum class EditorTool {
    TIMELINE, TRIM, TEXT, MUSIC, FILTERS, EFFECTS, STICKERS, TRANSFORM, EXPORT, PROJECT
}

enum class PreviewQuality(val label: String) { AUTO("Auto"), LOW("Low"), MEDIUM("Medium"), HIGH("High") }

enum class EditorFilter { NONE, GRAYSCALE, BRIGHTNESS_UP, CONTRAST_UP }

enum class TransitionType { NONE, FADE_IN_OUT, CROSS_DISSOLVE }

enum class ExportResolution(val label: String) { P720("1280x720"), P1080("1920x1080") }

data class ProjectSnapshot(
    val videoUri: String,
    val clips: List<TimelineClip>,
    val textLayers: List<TextLayer>,
    val selectedFilter: EditorFilter,
    val transitionType: TransitionType,
    val volume: Float,
    val speed: Float
)

data class VideoEditorUiState(
    val videoUri: String = "",
    val isPlaying: Boolean = false,
    val loopPlayback: Boolean = false,
    val currentTimeMs: Long = 0,
    val totalDurationMs: Long = 60_000,
    val timelineZoom: Float = 1f,
    val isPremium: Boolean = false,
    val clips: List<TimelineClip> = emptyList(),
    val selectedClipId: String? = null,
    val selectedTool: EditorTool = EditorTool.TIMELINE,
    val previewQuality: PreviewQuality = PreviewQuality.AUTO,
    val selectedFilter: EditorFilter = EditorFilter.NONE,
    val transitionType: TransitionType = TransitionType.NONE,
    val opacity: Float = 1f,
    val scale: Float = 1f,
    val rotation: Float = 0f,
    val positionX: Float = 0f,
    val positionY: Float = 0f,
    val volume: Float = 1f,
    val muted: Boolean = false,
    val speed: Float = 1f,
    val textLayers: List<TextLayer> = emptyList(),
    val activeTextLayerId: String? = null,
    val exportResolution: ExportResolution = ExportResolution.P720,
    val exportQuality: Int = 75,
    val projectJson: String = "",
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val donationUrl: String = "https://buy.stripe.com/test_donation_link"
)

@HiltViewModel
class VideoEditorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val gson = Gson()
    private val undoStack = ArrayDeque<VideoEditorUiState>()
    private val redoStack = ArrayDeque<VideoEditorUiState>()

    private val _uiState = MutableStateFlow(
        savedStateHandle["editor_state"] ?: VideoEditorUiState(
            clips = demoClips(),
            selectedClipId = demoClips().first().id
        )
    )
    val uiState: StateFlow<VideoEditorUiState> = _uiState.asStateFlow()

    private fun pushHistory() {
        undoStack.addLast(_uiState.value)
        if (undoStack.size > 40) undoStack.removeFirst()
        redoStack.clear()
        refreshHistoryState()
    }

    private fun refreshHistoryState() {
        _uiState.value = _uiState.value.copy(
            canUndo = undoStack.isNotEmpty(),
            canRedo = redoStack.isNotEmpty()
        )
        savedStateHandle["editor_state"] = _uiState.value
    }

    fun loadVideo(uri: String) {
        _uiState.value = _uiState.value.copy(videoUri = uri)
        refreshHistoryState()
    }

    fun togglePlayPause() {
        _uiState.value = _uiState.value.copy(isPlaying = !_uiState.value.isPlaying)
        refreshHistoryState()
    }

    fun seekTo(timeMs: Long) {
        _uiState.value = _uiState.value.copy(currentTimeMs = timeMs.coerceIn(0, _uiState.value.totalDurationMs))
        refreshHistoryState()
    }

    fun frameStep(forward: Boolean) {
        val step = if (forward) 33L else -33L
        seekTo(_uiState.value.currentTimeMs + step)
    }

    fun toggleLoopPlayback() {
        _uiState.value = _uiState.value.copy(loopPlayback = !_uiState.value.loopPlayback)
        refreshHistoryState()
    }

    fun selectTool(tool: EditorTool) {
        _uiState.value = _uiState.value.copy(selectedTool = tool)
        refreshHistoryState()
    }

    fun setPreviewQuality(quality: PreviewQuality) {
        _uiState.value = _uiState.value.copy(previewQuality = quality)
        refreshHistoryState()
    }

    fun setTimelineZoom(value: Float) {
        _uiState.value = _uiState.value.copy(timelineZoom = value.coerceIn(0.5f, 3f))
        refreshHistoryState()
    }

    fun selectClip(clipId: String) {
        _uiState.value = _uiState.value.copy(selectedClipId = clipId)
        refreshHistoryState()
    }

    fun reorderClip(fromIndex: Int, toIndex: Int) {
        val clips = _uiState.value.clips.toMutableList()
        if (fromIndex !in clips.indices || toIndex !in clips.indices) return
        pushHistory()
        val moved = clips.removeAt(fromIndex)
        clips.add(toIndex, moved)
        _uiState.value = _uiState.value.copy(clips = clips)
        refreshHistoryState()
    }

    fun trimStart(deltaMs: Long = 500) {
        val clipId = _uiState.value.selectedClipId ?: return
        pushHistory()
        _uiState.value = _uiState.value.copy(
            clips = _uiState.value.clips.map {
                if (it.id == clipId) {
                    val nextDuration = (it.durationMs - deltaMs).coerceAtLeast(500)
                    it.copy(startMs = it.startMs + deltaMs, durationMs = nextDuration)
                } else it
            }
        )
        refreshHistoryState()
    }

    fun trimEnd(deltaMs: Long = 500) {
        val clipId = _uiState.value.selectedClipId ?: return
        pushHistory()
        _uiState.value = _uiState.value.copy(
            clips = _uiState.value.clips.map {
                if (it.id == clipId) it.copy(durationMs = (it.durationMs - deltaMs).coerceAtLeast(500)) else it
            }
        )
        refreshHistoryState()
    }

    fun splitAtCurrentPosition() {
        val state = _uiState.value
        val clipId = state.selectedClipId ?: return
        val clipIndex = state.clips.indexOfFirst { it.id == clipId }
        if (clipIndex == -1) return
        val clip = state.clips[clipIndex]
        val localCut = (state.currentTimeMs - clip.startMs).coerceIn(500, clip.durationMs - 500)
        pushHistory()
        val first = clip.copy(durationMs = localCut)
        val second = clip.copy(
            id = UUID.randomUUID().toString(),
            name = "${clip.name} (B)",
            startMs = clip.startMs + localCut,
            durationMs = clip.durationMs - localCut
        )
        val updated = state.clips.toMutableList().apply {
            removeAt(clipIndex)
            add(clipIndex, second)
            add(clipIndex, first)
        }
        _uiState.value = state.copy(clips = updated, selectedClipId = second.id)
        refreshHistoryState()
    }

    fun deleteSelectedClip() {
        val clipId = _uiState.value.selectedClipId ?: return
        pushHistory()
        val updated = _uiState.value.clips.filterNot { it.id == clipId }
        _uiState.value = _uiState.value.copy(clips = updated, selectedClipId = updated.firstOrNull()?.id)
        refreshHistoryState()
    }

    fun setFilter(filter: EditorFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        refreshHistoryState()
    }

    fun setTransition(transition: TransitionType) {
        _uiState.value = _uiState.value.copy(transitionType = transition)
        refreshHistoryState()
    }

    fun setOpacity(value: Float) {
        _uiState.value = _uiState.value.copy(opacity = value.coerceIn(0f, 1f))
        refreshHistoryState()
    }

    fun setScale(value: Float) {
        _uiState.value = _uiState.value.copy(scale = value.coerceIn(0.3f, 2.5f))
        refreshHistoryState()
    }

    fun setRotation(value: Float) {
        _uiState.value = _uiState.value.copy(rotation = value.coerceIn(-180f, 180f))
        refreshHistoryState()
    }

    fun setPosition(x: Float, y: Float) {
        _uiState.value = _uiState.value.copy(positionX = x.coerceIn(-1f, 1f), positionY = y.coerceIn(-1f, 1f))
        refreshHistoryState()
    }

    fun setVolume(value: Float) {
        _uiState.value = _uiState.value.copy(volume = value.coerceIn(0f, 1f), muted = false)
        refreshHistoryState()
    }

    fun toggleMute() {
        _uiState.value = _uiState.value.copy(muted = !_uiState.value.muted)
        refreshHistoryState()
    }

    fun setSpeed(value: Float) {
        _uiState.value = _uiState.value.copy(speed = value.coerceIn(0.25f, 3f))
        refreshHistoryState()
    }

    fun addTextLayer(text: String) {
        if (text.isBlank()) return
        pushHistory()
        val layer = TextLayer(
            text = text,
            colorHex = 0xFFFFFFFF,
            fontSize = 24f,
            posX = 0f,
            posY = 0f,
            animation = TextAnimation.FADE_IN
        )
        _uiState.value = _uiState.value.copy(
            textLayers = _uiState.value.textLayers + layer,
            activeTextLayerId = layer.id
        )
        refreshHistoryState()
    }

    fun updateTextStyle(fontSize: Float? = null, colorHex: Long? = null, animation: TextAnimation? = null) {
        val id = _uiState.value.activeTextLayerId ?: return
        pushHistory()
        _uiState.value = _uiState.value.copy(
            textLayers = _uiState.value.textLayers.map {
                if (it.id == id) {
                    it.copy(
                        fontSize = fontSize ?: it.fontSize,
                        colorHex = colorHex ?: it.colorHex,
                        animation = animation ?: it.animation
                    )
                } else it
            }
        )
        refreshHistoryState()
    }

    fun selectTextLayer(layerId: String) {
        _uiState.value = _uiState.value.copy(activeTextLayerId = layerId)
        refreshHistoryState()
    }

    fun exportProjectJson() {
        val state = _uiState.value
        val snapshot = ProjectSnapshot(
            videoUri = state.videoUri,
            clips = state.clips,
            textLayers = state.textLayers,
            selectedFilter = state.selectedFilter,
            transitionType = state.transitionType,
            volume = state.volume,
            speed = state.speed
        )
        _uiState.value = state.copy(projectJson = gson.toJson(snapshot))
        refreshHistoryState()
    }

    fun importProjectJson(json: String) {
        runCatching {
            gson.fromJson(json, ProjectSnapshot::class.java)
        }.onSuccess { snapshot ->
            pushHistory()
            _uiState.value = _uiState.value.copy(
                videoUri = snapshot.videoUri,
                clips = snapshot.clips,
                selectedClipId = snapshot.clips.firstOrNull()?.id,
                textLayers = snapshot.textLayers,
                activeTextLayerId = snapshot.textLayers.firstOrNull()?.id,
                selectedFilter = snapshot.selectedFilter,
                transitionType = snapshot.transitionType,
                volume = snapshot.volume,
                speed = snapshot.speed,
                projectJson = json
            )
            refreshHistoryState()
        }
    }

    fun setExportResolution(resolution: ExportResolution) {
        _uiState.value = _uiState.value.copy(exportResolution = resolution)
        refreshHistoryState()
    }

    fun setExportQuality(quality: Int) {
        _uiState.value = _uiState.value.copy(exportQuality = quality.coerceIn(35, 100))
        refreshHistoryState()
    }

    fun triggerAiMagicEdit() {
        pushHistory()
        _uiState.value = _uiState.value.copy(
            selectedFilter = EditorFilter.CONTRAST_UP,
            transitionType = TransitionType.CROSS_DISSOLVE,
            speed = 1.1f
        )
        refreshHistoryState()
    }

    fun startExport() {
        exportProjectJson()
    }

    fun undo() {
        val previous = undoStack.removeLastOrNull() ?: return
        redoStack.addLast(_uiState.value)
        _uiState.value = previous
        refreshHistoryState()
    }

    fun redo() {
        val next = redoStack.removeLastOrNull() ?: return
        undoStack.addLast(_uiState.value)
        _uiState.value = next
        refreshHistoryState()
    }

    private fun demoClips() = listOf(
        TimelineClip(name = "Intro.mp4", track = 0, startMs = 0, durationMs = 10_000, colorHex = VibeCutColors.TimelineTrack1.value.toLong()),
        TimelineClip(name = "B-Roll.mp4", track = 0, startMs = 10_000, durationMs = 12_000, colorHex = VibeCutColors.TimelineTrack2.value.toLong()),
        TimelineClip(name = "Voice Over.wav", track = 1, startMs = 0, durationMs = 22_000, colorHex = VibeCutColors.TimelineTrack3.value.toLong())
    )
}