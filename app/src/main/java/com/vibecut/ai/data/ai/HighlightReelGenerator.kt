package com.vibecut.ai.data.ai

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.min
import javax.inject.Inject
import javax.inject.Singleton

data class VideoSegment(val startMs: Long, val endMs: Long, val score: Float)

@Singleton
class HighlightReelGenerator @Inject constructor() {

    /**
     * Scores each second of video by motion (frame difference).
     * Picks top-scoring segments to build a highlight reel.
     * Completely on-device — no AI backend needed.
     */
    suspend fun generateHighlights(
        videoPath: String,
        targetDurationMs: Long = 30_000,
        segmentLengthMs: Long = 3_000
    ): List<VideoSegment> = withContext(Dispatchers.Default) {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoPath)

        val totalMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            ?.toLong() ?: return@withContext emptyList()

        val scoredSegments = mutableListOf<VideoSegment>()
        var timeMs = 0L

        while (timeMs + segmentLengthMs <= totalMs) {
            val midMs = timeMs + segmentLengthMs / 2
            val frameA = retriever.getFrameAtTime(timeMs * 1000L, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            val frameB = retriever.getFrameAtTime(midMs * 1000L, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)

            val score = if (frameA != null && frameB != null) {
                computeMotionScore(frameA, frameB)
            } else 0f

            scoredSegments.add(VideoSegment(timeMs, timeMs + segmentLengthMs, score))
            timeMs += segmentLengthMs
        }

        retriever.release()

        // Pick top segments by score, ensure no overlap
        val maxSegments = (targetDurationMs / segmentLengthMs).toInt()
        selectTopNonOverlapping(scoredSegments, maxSegments)
    }

    /**
     * Computes motion score between two frames by comparing pixel brightness.
     * Higher score = more motion = more interesting for highlights.
     */
    private fun computeMotionScore(frameA: Bitmap, frameB: Bitmap): Float {
        val width = min(frameA.width, 32)
        val height = min(frameA.height, 32)
        val a = Bitmap.createScaledBitmap(frameA, width, height, false)
        val b = Bitmap.createScaledBitmap(frameB, width, height, false)

        var totalDiff = 0L
        for (x in 0 until width) {
            for (y in 0 until height) {
                val pa = a.getPixel(x, y)
                val pb = b.getPixel(x, y)
                val dr = abs((pa shr 16 and 0xFF) - (pb shr 16 and 0xFF))
                val dg = abs((pa shr 8 and 0xFF) - (pb shr 8 and 0xFF))
                val db = abs((pa and 0xFF) - (pb and 0xFF))
                totalDiff += dr + dg + db
            }
        }
        return (totalDiff.toFloat() / (width * height * 3 * 255)).coerceIn(0f, 1f)
    }

    private fun selectTopNonOverlapping(segments: List<VideoSegment>, maxCount: Int): List<VideoSegment> {
        val sorted = segments.sortedByDescending { it.score }
        val selected = mutableListOf<VideoSegment>()
        for (seg in sorted) {
            if (selected.size >= maxCount) break
            val overlaps = selected.any { s -> seg.startMs < s.endMs && seg.endMs > s.startMs }
            if (!overlaps) selected.add(seg)
        }
        return selected.sortedBy { it.startMs }
    }
}