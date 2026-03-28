package com.vibecut.ai.data.video

import android.graphics.Color
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class ChromakeyProcessor @Inject constructor() {

    suspend fun applyChromakey(
        inputPath: String,
        backgroundPath: String,
        outputPath: String,
        keyColor: Int = Color.GREEN,
        similarity: Float = 0.3f,
        blend: Float = 0.1f
    ): Result<String> = withContext(Dispatchers.IO) {
        val hex = colorToHex(keyColor)
        val sim = "%.2f".format(similarity.coerceIn(0.01f, 1f))
        val blendStr = "%.2f".format(blend.coerceIn(0f, 1f))

        val command = "-i \"$inputPath\" -i \"$backgroundPath\" -filter_complex \"[0:v]chromakey=color=$hex:similarity=$sim:blend=$blendStr[fg]; [1:v]scale=iw:ih[bg]; [bg][fg]overlay[out]\" -map \"[out]\" -map 0:a? -c:v libx264 -preset fast -crf 22 -c:a copy \"$outputPath\""

        suspendCancellableCoroutine { cont ->
            val session = FFmpegKit.executeAsync(command) { s ->
                if (ReturnCode.isSuccess(s.returnCode)) cont.resume(Result.success(outputPath))
                else cont.resume(Result.failure(Exception("Chromakey failed: ${s.failStackTrace}")))
            }
            cont.invokeOnCancellation { FFmpegKit.cancel(session.sessionId) }
        }
    }

    fun sampleColorAtPoint(bitmap: android.graphics.Bitmap, x: Float, y: Float): Int {
        val px = (x * bitmap.width).toInt().coerceIn(0, bitmap.width - 1)
        val py = (y * bitmap.height).toInt().coerceIn(0, bitmap.height - 1)
        return bitmap.getPixel(px, py)
    }

    private fun colorToHex(color: Int): String {
        val r = (color shr 16) and 0xFF
        val g = (color shr 8) and 0xFF
        val b = color and 0xFF
        return "0x%02X%02X%02X".format(r, g, b)
    }
}