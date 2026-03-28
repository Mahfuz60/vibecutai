package com.vibecut.ai.domain.repository

import com.vibecut.ai.domain.model.AiMagicResult
import com.vibecut.ai.domain.model.Caption

data class MusicSuggestion(
    val title: String,
    val genre: String,
    val bpm: Int,
    val description: String
)

interface AiRepository {
    suspend fun runMagicEdit(videoPath: String, userId: String): Result<AiMagicResult>

    suspend fun generateCaptions(
        audioPath: String,
        language: String,
        userId: String
    ): Result<List<Caption>>

    suspend fun removeBackground(
        framePath: String,
        userId: String
    ): Result<String>

    suspend fun suggestMusic(videoMood: String): Result<List<MusicSuggestion>>

    suspend fun generateHighlightReel(
        clips: List<String>,
        duration: Int,
        userId: String
    ): Result<String>
}