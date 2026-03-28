package com.vibecut.ai.domain.repository

import com.vibecut.ai.data.repository.UserSession
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<UserSession>

    suspend fun signIn(email: String, password: String): Result<UserSession>

    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<UserSession>

    suspend fun signInAsGuest(): Result<UserSession>

    suspend fun signOut()
}