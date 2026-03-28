package com.vibecut.ai.data.effects

import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class CloneEffect @Inject constructor() {

    /**
     * Creates a "clone/echo" effect by overlaying a semi-transparent delayed copy
     * of the video on top of itself using FFmpegKit blend filters.
     * No AI or API needed — pure FFmpegKit filter_complex.
     */
    suspend fun applyCloneEffect(
        inputPath: String,
        outputPath: String,
        delayMs: Int = 500,
        ghostAlpha: Float = 0.4f
    ): Result<String> = withContext(Dispatchers.IO) {
        val alphaStr = "%.2f".format(ghostAlpha)
        val delaySec = delayMs / 1000.0

        // FFmpegKit clone filter: blend delayed copy with original
        val command = "-i \"$inputPath\" -filter_complex \"[0:v]split=2[original][delayed]; [delayed]setpts=PTS+$delaySec/TB,format=rgba,colorchannelmixer=aa=$alphaStr[ghost]; [original][ghost]overlay=0:0:format=rgb[out]\" -map \"[out]\" -map 0:a? -c:v libx264 -preset fast -crf 22 -c:a copy \"$outputPath\""

        suspendCancellableCoroutine { cont ->
            val session = FFmpegKit.executeAsync(command) { s ->
                if (ReturnCode.isSuccess(s.returnCode)) {
                    cont.resume(Result.success(outputPath))
                } else {
                    cont.resume(Result.failure(Exception("Clone effect failed: ${s.failStackTrace}")))
                }
            }
            cont.invokeOnCancellation { FFmpegKit.cancel(session.sessionId) }
        }
    }

    /**
     * Mirror split-screen effect: original left, flipped clone right.
     */
    suspend fun applyMirrorEffect(inputPath: String, outputPath: String): Result<String> =
        withContext(Dispatchers.IO) {
            val command = "-i \"$inputPath\" -filter_complex \"[0:v]split=2[left][right]; [right]hflip[rflipped]; [left][rflipped]hstack=inputs=2[out]\" -map \"[out]\" -map 0:a? -c:v libx264 -preset fast \"$outputPath\""

            suspendCancellableCoroutine { cont ->
                val session = FFmpegKit.executeAsync(command) { s ->
                    if (ReturnCode.isSuccess(s.returnCode)) cont.resume(Result.success(outputPath))
                    else cont.resume(Result.failure(Exception(s.failStackTrace)))
                }
                cont.invokeOnCancellation { FFmpegKit.cancel(session.sessionId) }
            }
        }
}