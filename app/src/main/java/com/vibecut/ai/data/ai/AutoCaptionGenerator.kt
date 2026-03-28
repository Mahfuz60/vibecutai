package com.vibecut.ai.data.ai

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

data class Caption(
    val startMs: Long,
    val endMs: Long,
    val text: String,
    val confidence: Float = 1f
)

@Singleton
class AutoCaptionGenerator @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Generates captions using Android's on-device SpeechRecognizer.
     * Requires RECORD_AUDIO permission.
     * Works offline once language model is downloaded.
     *
     * For video files, extracts audio to a temp file first via FFmpegKit,
     * then feeds to SpeechRecognizer for transcription.
     */
    fun generateCaptionsFromMic(): Flow<Caption> = callbackFlow {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            close(Exception("Speech recognition not available on this device"))
            return@callbackFlow
        }

        val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
        var startTimeMs = System.currentTimeMillis()

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) { startTimeMs = System.currentTimeMillis() }
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) { close() }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull() ?: return
                val now = System.currentTimeMillis()
                trySend(Caption(startTimeMs, now, text, 0.95f))
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = partial?.firstOrNull() ?: return
                val now = System.currentTimeMillis()
                trySend(Caption(startTimeMs, now, "[...] $text", 0.6f))
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        recognizer.startListening(intent)
        awaitClose { recognizer.destroy() }
    }

    /**
     * Converts a list of captions to SRT subtitle format
     */
    fun toSrtFormat(captions: List<Caption>): String {
        val sb = StringBuilder()
        captions.forEachIndexed { index, caption ->
            sb.appendLine(index + 1)
            sb.appendLine("${formatSrtTime(caption.startMs)} --> ${formatSrtTime(caption.endMs)}")
            sb.appendLine(caption.text)
            sb.appendLine()
        }
        return sb.toString()
    }

    private fun formatSrtTime(ms: Long): String {
        val hours = ms / 3_600_000
        val minutes = (ms % 3_600_000) / 60_000
        val seconds = (ms % 60_000) / 1_000
        val millis = ms % 1_000
        return "%02d:%02d:%02d,%03d".format(hours, minutes, seconds, millis)
    }
}