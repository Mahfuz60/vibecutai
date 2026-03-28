package com.vibecut.ai.data.video

import android.content.Context
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class FFmpegProcessor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private fun outputPath(name: String): String =
        File(context.filesDir, "exports/$name.mp4").also { it.parentFile?.mkdirs() }.absolutePath

    suspend fun trimVideo(
        inputPath: String,
        startMs: Long,
        endMs: Long,
        outputName: String = "trimmed_${System.currentTimeMillis()}"
    ): Result<String> = withContext(Dispatchers.IO) {
        val output = outputPath(outputName)
        val startSec = startMs / 1000.0
        val durationSec = (endMs - startMs) / 1000.0
        executeCommand("-i \"$inputPath\" -ss $startSec -t $durationSec -c:v libx264 -preset fast -crf 23 -c:a aac \"$output\"", output)
    }

    suspend fun mergeVideos(inputPaths: List<String>, outputName: String = "merged_${System.currentTimeMillis()}"): Result<String> = withContext(Dispatchers.IO) {
        val output = outputPath(outputName)
        val listFile = File(context.cacheDir, "merge_list.txt")
        listFile.writeText(inputPaths.joinToString("\n") { "file '$it'" })
        executeCommand("-f concat -safe 0 -i \"${listFile.absolutePath}\" -c copy \"$output\"", output)
    }

    suspend fun reverseVideo(inputPath: String, outputName: String = "reversed_${System.currentTimeMillis()}"): Result<String> = withContext(Dispatchers.IO) {
        val output = outputPath(outputName)
        executeCommand("-i \"$inputPath\" -vf reverse -af areverse \"$output\"", output)
    }

    suspend fun changeSpeed(inputPath: String, speed: Float, outputName: String = "speed_${System.currentTimeMillis()}"): Result<String> = withContext(Dispatchers.IO) {
        val output = outputPath(outputName)
        val videoFilter = "setpts=${1.0 / speed}*PTS"
        val audioFilter = "atempo=${speed.coerceIn(0.5f, 2.0f)}"
        executeCommand("-i \"$inputPath\" -vf \"$videoFilter\" -af \"$audioFilter\" \"$output\"", output)
    }

    suspend fun exportHD(inputPath: String, is4K: Boolean, outputName: String = "export_${System.currentTimeMillis()}"): Result<String> = withContext(Dispatchers.IO) {
        val output = outputPath(outputName)
        val scaleFilter = if (is4K) "scale=3840:2160" else "scale=1920:1080"
        executeCommand("-i \"$inputPath\" -vf \"$scaleFilter\" -c:v libx264 -preset slow -crf 18 -c:a aac -b:a 192k \"$output\"", output)
    }

    suspend fun addMusicToVideo(videoPath: String, musicPath: String, volume: Float = 0.8f, outputName: String = "with_music_${System.currentTimeMillis()}"): Result<String> = withContext(Dispatchers.IO) {
        val output = outputPath(outputName)
        executeCommand("-i \"$videoPath\" -i \"$musicPath\" -filter_complex \"[1:a]volume=$volume[music];[0:a][music]amix=inputs=2:duration=first:dropout_transition=2\" -c:v copy \"$output\"", output)
    }

    suspend fun addSubtitles(videoPath: String, srtPath: String, outputName: String = "subtitled_${System.currentTimeMillis()}"): Result<String> = withContext(Dispatchers.IO) {
        val output = outputPath(outputName)
        executeCommand("-i \"$videoPath\" -vf \"subtitles='$srtPath':force_style='FontSize=20,PrimaryColour=&HFFFFFF&'\" -c:a copy \"$output\"", output)
    }

    private suspend fun executeCommand(command: String, expectedOutput: String): Result<String> =
        suspendCancellableCoroutine { cont ->
            val session = FFmpegKit.executeAsync(command) { session ->
                if (ReturnCode.isSuccess(session.returnCode)) {
                    Timber.d("FFmpeg success: $expectedOutput")
                    cont.resume(Result.success(expectedOutput))
                } else {
                    val error = session.failStackTrace ?: "FFmpeg failed"
                    Timber.e("FFmpeg error: $error")
                    cont.resume(Result.failure(Exception(error)))
                }
            }
            cont.invokeOnCancellation { FFmpegKit.cancel(session.sessionId) }
        }
}