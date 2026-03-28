package com.vibecut.ai.data.ai

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import javax.inject.Inject
import javax.inject.Singleton

data class SceneBoundary(val timeMs: Long, val confidence: Float)

@Singleton
class SceneDetector @Inject constructor() {

    private val detector = ObjectDetection.getClient(
        ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .build()
    )

    /**
     * Detects scene boundaries purely on-device.
     * Samples one frame per second and computes pixel histogram difference.
     * No API calls — works fully offline.
     */
    suspend fun detectScenes(videoPath: String, sampleIntervalMs: Long = 1000): List<SceneBoundary> =
        withContext(Dispatchers.Default) {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(videoPath)

            val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toLong() ?: return@withContext emptyList()

            val boundaries = mutableListOf<SceneBoundary>()
            var prevHistogram: FloatArray? = null

            var timeMs = 0L
            while (timeMs <= durationMs) {
                val frame = retriever.getFrameAtTime(
                    timeMs * 1000L,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                )
                
                if (frame == null) {
                    timeMs += sampleIntervalMs
                    continue
                }

                val histogram = computeHistogram(frame)
                prevHistogram?.let { prev ->
                    val diff = histogramDifference(prev, histogram)
                    if (diff > SCENE_CHANGE_THRESHOLD) {
                        boundaries.add(SceneBoundary(timeMs, diff))
                    }
                }
                prevHistogram = histogram
                timeMs += sampleIntervalMs
            }

            retriever.release()
            boundaries
        }

    /** Detects silent segments where audio amplitude is below threshold */
    fun detectSilentSegments(amplitudes: List<Pair<Long, Float>>, threshold: Float = 0.05f, minDurationMs: Long = 500): List<LongRange> {
        val silentRanges = mutableListOf<LongRange>()
        var silentStart: Long? = null

        amplitudes.forEach { (timeMs, amplitude) ->
            if (amplitude < threshold) {
                if (silentStart == null) silentStart = timeMs
            } else {
                silentStart?.let { start ->
                    if (timeMs - start >= minDurationMs) {
                        silentRanges.add(start..timeMs)
                    }
                    silentStart = null
                }
            }
        }
        return silentRanges
    }

    private fun computeHistogram(bitmap: Bitmap): FloatArray {
        val histogram = FloatArray(256)
        val scaled = Bitmap.createScaledBitmap(bitmap, 64, 64, false)
        for (x in 0 until scaled.width) {
            for (y in 0 until scaled.height) {
                val pixel = scaled.getPixel(x, y)
                val gray = ((pixel shr 16 and 0xFF) * 0.299 +
                        (pixel shr 8 and 0xFF) * 0.587 +
                        (pixel and 0xFF) * 0.114).toInt()
                histogram[gray]++
            }
        }
        val total = histogram.sum()
        return FloatArray(256) { histogram[it] / total }
    }

    private fun histogramDifference(a: FloatArray, b: FloatArray): Float =
        a.zip(b.toList()).sumOf { (x, y) -> abs(x - y).toDouble() }.toFloat()

    companion object {
        private const val SCENE_CHANGE_THRESHOLD = 0.35f
    }
}