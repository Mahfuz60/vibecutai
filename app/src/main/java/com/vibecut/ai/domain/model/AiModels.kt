package com.vibecut.ai.domain.model

data class AiMagicResult(
    val suggestedCuts: List<String> = emptyList(),
    val recommendedMusic: String = ""
)

data class Caption(
    val startMs: Long = 0L,
    val endMs: Long = 0L,
    val text: String = ""
)