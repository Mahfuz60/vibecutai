package com.vibecut.ai.data.ai

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.sqrt
import javax.inject.Inject
import javax.inject.Singleton

data class Beat(val timeMs: Long, val energy: Float)

@Singleton
class BeatSyncAnalyzer @Inject constructor() {

    companion object {
        private const val SAMPLE_RATE = 44100
        private const val BUFFER_SIZE_FRAMES = 1024
        private const val ENERGY_THRESHOLD_MULTIPLIER = 1.5f
        private const val HISTORY_SIZE = 43  // ~1 second at 44100/1024
    }

    /**
     * Detects beat timestamps from a live audio recording session.
     * Uses energy-based onset detection — compares current energy
     * against a rolling average. No ML or API required.
     *
     * Usage: Start recording, call this, stop when done.
     */
    suspend fun detectBeatsFromMicrophone(durationMs: Long): List<Beat> =
        withContext(Dispatchers.IO) {
            val bufferSize = AudioRecord.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            val recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            val beats = mutableListOf<Beat>()
            val energyHistory = ArrayDeque<Float>(HISTORY_SIZE)
            val buffer = ShortArray(BUFFER_SIZE_FRAMES)
            val startTime = System.currentTimeMillis()

            recorder.startRecording()

            while (System.currentTimeMillis() - startTime < durationMs) {
                val read = recorder.read(buffer, 0, buffer.size)
                if (read <= 0) continue

                val energy = computeRmsEnergy(buffer, read)
                val avgEnergy = if (energyHistory.size > 0) energyHistory.average().toFloat() else energy

                if (energy > avgEnergy * ENERGY_THRESHOLD_MULTIPLIER && energy > 0.01f) {
                    beats.add(Beat(System.currentTimeMillis() - startTime, energy))
                }

                energyHistory.addLast(energy)
                if (energyHistory.size > HISTORY_SIZE) energyHistory.removeFirst()
            }

            recorder.stop()
            recorder.release()

            deduplicateBeats(beats, minIntervalMs = 250)
        }

    /**
     * Generates cut points for a video based on beat timestamps.
     * Each beat becomes a potential cut point for beat-sync editing.
     */
    fun generateCutPoints(beats: List<Beat>, videoDurationMs: Long): List<Long> {
        return beats
            .filter { it.timeMs < videoDurationMs }
            .map { it.timeMs }
            .sorted()
    }

    private fun computeRmsEnergy(buffer: ShortArray, length: Int): Float {
        val sumOfSquares = buffer.take(length).sumOf { (it.toFloat() / Short.MAX_VALUE).let { s -> (s * s).toDouble() } }
        return sqrt(sumOfSquares / length).toFloat()
    }

    private fun deduplicateBeats(beats: List<Beat>, minIntervalMs: Long): List<Beat> {
        val result = mutableListOf<Beat>()
        var lastBeatTime = -minIntervalMs
        beats.sortedBy { it.timeMs }.forEach { beat ->
            if (beat.timeMs - lastBeatTime >= minIntervalMs) {
                result.add(beat)
                lastBeatTime = beat.timeMs
            }
        }
        return result
    }
}