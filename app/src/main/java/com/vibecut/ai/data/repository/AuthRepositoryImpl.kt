package com.vibecut.ai.data.repository

import com.vibecut.ai.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserSession(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val isGuest: Boolean = true,
    val isLoggedIn: Boolean = false
)

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    private val _currentUser = MutableStateFlow(UserSession())
    override val currentUser: StateFlow<UserSession> = _currentUser.asStateFlow()

    override suspend fun signIn(email: String, password: String): Result<UserSession> {
        val user = UserSession(
            id = "local-user",
            name = email.substringBefore("@").ifBlank { "User" },
            email = email,
            isGuest = false,
            isLoggedIn = true
        )
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<UserSession> {
        val user = UserSession(
            id = "local-user",
            name = name,
            email = email,
            isGuest = false,
            isLoggedIn = true
        )
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun signInAsGuest(): Result<UserSession> {
        val user = UserSession(
            id = "guest",
            name = "Guest",
            email = "",
            isGuest = true,
            isLoggedIn = true
        )
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun signOut() {
        _currentUser.value = UserSession()
    }
}