package com.vibecut.ai.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibecut.ai.data.repository.UserSession
import com.vibecut.ai.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val currentUser: UserSession = UserSession(),
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                _uiState.update {
                    it.copy(
                        currentUser = user,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        runAuth { authRepository.signIn(email, password) }
    }

    fun signUp(name: String, email: String, password: String) {
        runAuth { authRepository.signUp(name, email, password) }
    }

    fun continueAsGuest() {
        runAuth { authRepository.signInAsGuest() }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.update { it.copy(errorMessage = null) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun runAuth(block: suspend () -> Result<UserSession>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            block()
                .onSuccess { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentUser = user,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Authentication failed"
                        )
                    }
                }
        }
    }
}