package com.vibecut.ai.data.video

import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

enum class Transition(val ffmpegName: String, val label: String, val emoji: String) {
    FADE("fade", "Fade", "🌅"),
    DISSOLVE("dissolve", "Dissolve", "💫"),
    WIPE_LEFT("wipeleft", "Wipe Left", "⬅️"),
    WIPE_RIGHT("wiperight", "Wipe Right", "➡️"),
    WIPE_UP("wipeup", "Wipe Up", "⬆️"),
    WIPE_DOWN("wipedown", "Wipe Down", "⬇️"),
    SLIDE_LEFT("slideleft", "Slide Left", "🎞️"),
    SLIDE_RIGHT("slideright", "Slide Right", "🎬"),
    SLIDE_UP("slideup", "Slide Up", "📤"),
    SLIDE_DOWN("slidedown", "Slide Down", "📥"),
    ZOOM_IN("smoothup", "Zoom In", "🔍"),
    ZOOM_OUT("smoothdown", "Zoom Out", "🔎"),
    CIRCLE_CROP("circlecrop", "Circle", "⭕"),
    RECT_CROP("rectcrop", "Rect Crop", "▪️"),
    DISTANCE("distance", "Distance", "📡"),
    FADE_GRAYS("fadegrays", "Fade Gray", "🩶"),
    PIXEL_ELIZE("pixelize", "Pixelize", "🎮"),
    RADIAL("radial", "Radial", "🌀"),
    HBLUR("hblur", "H-Blur", "🌫️"),
    WIND("wind", "Wind", "💨"),
    SQUEEZEH("squeezeh", "Squeeze H", "↔️"),
    SQUEEZEV("squeezev", "Squeeze V", "↕️"),
    HLSLICE("hlslice", "HL Slice", "✂️"),
    HRSLICE("hrslice", "HR Slice", "✂️"),
    VUSLICE("vuslice", "VU Slice", "✂️"),
    VDSLICE("vdslice", "VD Slice", "✂️"),
    DIAGTL("diagtl", "Diag TL", "↖️"),
    DIAGTR("diagtr", "Diag TR", "↗️"),
    DIAGBL("diagbl", "Diag BL", "↙️"),
    DIAGBR("diagbr", "Diag BR", "↘️"),
}

@Singleton
class TransitionEngine @Inject constructor() {

    /**
     * Joins two video clips with a transition effect using FFmpegKit xfade.
     * No backend or API needed — all processing happens on-device.
     *
     * @param clipA First video path
     * @param clipB Second video path
     * @param transition Transition type
     * @param durationSec Duration of transition in seconds (0.5–2.0)
     * @param outputPath Output video path
     */
    suspend fun applyTransition(
        clipA: String,
        clipB: String,
        transition: Transition,
        durationSec: Double = 0.75,
        outputPath: String
    ): Result<String> = withContext(Dispatchers.IO) {
        // Get duration of clip A to compute offset
        val durationA = getVideoDurationSec(clipA)
        val offset = (durationA - durationSec).coerceAtLeast(0.0)

        // Note: In a production app, you should check if the files actually contain audio tracks.
        // For now, this assumes both clips have audio. If they don't, you need to remove the [a] mappings.
        val command = buildString {
            append("-i \"$clipA\" -i \"$clipB\" ")
            append("-filter_complex \"")
            append("[0:v][1:v]xfade=transition=${transition.ffmpegName}:duration=$durationSec:offset=$offset,format=yuv420p[v]; ")
            append("[0:a][1:a]acrossfade=d=$durationSec[a]")
            append("\" ")
            append("-map \"[v]\" -map \"[a]\" ")
            append("-c:v libx264 -preset fast -crf 22 \"$outputPath\"")
        }

        suspendCancellableCoroutine { cont ->
            val session = FFmpegKit.executeAsync(command) { s ->
                if (ReturnCode.isSuccess(s.returnCode)) cont.resume(Result.success(outputPath))
                else cont.resume(Result.failure(Exception("Transition failed: ${s.failStackTrace}")))
            }
            cont.invokeOnCancellation { FFmpegKit.cancel(session.sessionId) }
        }
    }

    /**
     * Merge multiple clips with transitions between each pair.
     */
    suspend fun mergeWithTransitions(
        clips: List<String>,
        transition: Transition,
        transitionDurationSec: Double = 0.75,
        outputPath: String
    ): Result<String> = withContext(Dispatchers.IO) {
        if (clips.size < 2) return@withContext Result.failure(Exception("Need at least 2 clips"))

        var current = clips[0]
        clips.drop(1).forEachIndexed { index, clip ->
            val tempOut = outputPath.replace(".mp4", "_t$index.mp4")
            applyTransition(current, clip, transition, transitionDurationSec, tempOut).getOrElse {
                return@withContext Result.failure(it)
            }
            current = tempOut
        }

        // Rename last to final output
        java.io.File(current).renameTo(java.io.File(outputPath))
        Result.success(outputPath)
    }

    private suspend fun getVideoDurationSec(videoPath: String): Double =
        withContext(Dispatchers.IO) {
            val retriever = android.media.MediaMetadataRetriever()
            retriever.setDataSource(videoPath)
            val ms = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
            retriever.release()
            ms / 1000.0
        }
}