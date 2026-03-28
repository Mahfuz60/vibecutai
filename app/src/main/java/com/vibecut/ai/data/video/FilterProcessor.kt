package com.vibecut.ai.data.video

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

enum class VideoFilter(
    val label: String,
    val emoji: String,
    val ffmpegFilter: String
) {
    NONE("Original", "🎬", "null"),
    VIVID("Vivid", "🌈", "eq=saturation=1.8:contrast=1.1"),
    CINEMATIC("Cinematic", "🎥", "eq=brightness=-0.05:contrast=1.2:saturation=0.85,vignette=PI/4"),
    VINTAGE("Vintage", "📷", "eq=brightness=0.05:saturation=0.6:contrast=0.9,curves=r='0/0.1 1/0.9':b='0/0.05 1/0.85',vignette=PI/3"),
    NOIR("Noir", "🖤", "hue=s=0,eq=contrast=1.4:brightness=-0.1,vignette=PI/3"),
    WARM("Warm", "🌅", "eq=saturation=1.2,curves=r='0/0 1/1':b='0/0 0.5/0.3 1/0.8'"),
    COOL("Cool", "❄️", "eq=saturation=1.1,curves=b='0/0.1 1/1.1':r='0/0 1/0.85'"),
    FADE("Fade", "🌫️", "curves=all='0/0.1 1/0.9',vignette=PI/5"),
    DREAMY("Dreamy", "✨", "gblur=sigma=2,eq=saturation=1.3:brightness=0.05"),
    DRAMATIC("Dramatic", "⚡", "eq=contrast=1.5:saturation=0.7:brightness=-0.1,vignette=PI/3"),
    GOLDEN_HOUR("Golden Hr", "🌇", "curves=r='0/0 0.5/0.7 1/1':g='0/0 0.5/0.5 1/0.85':b='0/0 0.5/0.3 1/0.6'"),
    TEAL_ORANGE("Teal & Orange", "🎞️", "curves=r='0/0 0.5/0.7 1/1':b='0/0.1 0.5/0.4 1/0.7',eq=saturation=1.3"),
    MATTE("Matte", "📽️", "curves=all='0/0.05 1/0.95',eq=saturation=0.9"),
    GLITCH_COLOR("Glitch", "📺", "rgbashift=rh=3:rv=0:gh=-2:gv=1:bh=2:bv=-1"),
    CYBERPUNK("Cyberpunk", "🤖", "hue=H=180,eq=saturation=2:contrast=1.3,colorlevels=rimin=0.1:gimin=0:bimin=0.1"),
    PASTEL("Pastel", "🌸", "eq=saturation=0.6:brightness=0.1:contrast=0.9"),
}

@Singleton
class FilterProcessor @Inject constructor() {

    suspend fun applyFilterToVideo(
        inputPath: String,
        outputPath: String,
        filter: VideoFilter
    ): Result<String> = withContext(Dispatchers.IO) {
        if (filter == VideoFilter.NONE) {
            java.io.File(inputPath).copyTo(java.io.File(outputPath), overwrite = true)
            return@withContext Result.success(outputPath)
        }

        val command = "-i \"$inputPath\" -vf \"${filter.ffmpegFilter}\" -c:v libx264 -preset fast -crf 22 -c:a copy \"$outputPath\""
        suspendCancellableCoroutine { cont ->
            val session = FFmpegKit.executeAsync(command) { s ->
                if (ReturnCode.isSuccess(s.returnCode)) cont.resume(Result.success(outputPath))
                else cont.resume(Result.failure(Exception(s.failStackTrace)))
            }
            cont.invokeOnCancellation { FFmpegKit.cancel(session.sessionId) }
        }
    }

    fun applyFilterToBitmap(bitmap: Bitmap, filter: VideoFilter): Bitmap {
        val colorMatrix = getColorMatrixForFilter(filter)
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(colorMatrix) }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return output
    }

    private fun getColorMatrixForFilter(filter: VideoFilter): ColorMatrix = when (filter) {
        VideoFilter.NOIR -> ColorMatrix().apply { setSaturation(0f) }
        VideoFilter.VIVID -> ColorMatrix().apply { setSaturation(2f) }
        VideoFilter.WARM -> ColorMatrix(floatArrayOf(
            1.2f, 0.1f, 0f, 0f, 10f,
            0f, 1.0f, 0f, 0f, 0f,
            0f, 0f, 0.8f, 0f, -10f,
            0f, 0f, 0f, 1f, 0f
        ))
        VideoFilter.COOL -> ColorMatrix(floatArrayOf(
            0.85f, 0f, 0.1f, 0f, -5f,
            0f, 0.95f, 0f, 0f, 0f,
            0.1f, 0f, 1.2f, 0f, 10f,
            0f, 0f, 0f, 1f, 0f
        ))
        VideoFilter.VINTAGE -> ColorMatrix().apply {
            setSaturation(0.6f)
            postConcat(ColorMatrix(floatArrayOf(
                1f, 0f, 0f, 0f, 20f,
                0f, 0.9f, 0f, 0f, 10f,
                0f, 0f, 0.7f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )))
        }
        else -> ColorMatrix()
    }
}