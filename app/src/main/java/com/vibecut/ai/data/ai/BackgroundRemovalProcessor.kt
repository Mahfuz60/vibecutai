package com.vibecut.ai.data.ai

import android.graphics.Bitmap
import android.graphics.Color
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import com.google.mlkit.vision.segmentation.Segmentation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class BackgroundRemovalProcessor @Inject constructor() {

    private val segmenter = Segmentation.getClient(
        SelfieSegmenterOptions.Builder()
            .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
            .enableRawSizeMask()
            .build()
    )

    suspend fun removeBackground(bitmap: Bitmap): Result<Bitmap> =
        suspendCancellableCoroutine { cont ->
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            segmenter.process(inputImage)
                .addOnSuccessListener { segmentationMask ->
                    val mask = segmentationMask.buffer
                    val maskWidth = segmentationMask.width
                    val maskHeight = segmentationMask.height

                    val outputBitmap = Bitmap.createBitmap(maskWidth, maskHeight, Bitmap.Config.ARGB_8888)
                    val scaledInput = Bitmap.createScaledBitmap(bitmap, maskWidth, maskHeight, true)

                    mask.rewind()
                    for (y in 0 until maskHeight) {
                        for (x in 0 until maskWidth) {
                            val foregroundConfidence = mask.float
                            val pixelColor = scaledInput.getPixel(x, y)
                            val alpha = (foregroundConfidence * 255).toInt()
                            outputBitmap.setPixel(x, y, Color.argb(alpha, Color.red(pixelColor), Color.green(pixelColor), Color.blue(pixelColor)))
                        }
                    }
                    cont.resume(Result.success(outputBitmap))
                }
                .addOnFailureListener { e ->
                    cont.resume(Result.failure(e))
                }
        }

    suspend fun replaceBackground(
        foregroundBitmap: Bitmap,
        backgroundBitmap: Bitmap
    ): Result<Bitmap> {
        val foregroundResult = removeBackground(foregroundBitmap)
        return foregroundResult.map { fg ->
            val output = backgroundBitmap.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = android.graphics.Canvas(output)
            val scaledFg = Bitmap.createScaledBitmap(fg, output.width, output.height, true)
            canvas.drawBitmap(scaledFg, 0f, 0f, null)
            output
        }
    }
}