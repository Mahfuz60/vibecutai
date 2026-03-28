package com.vibecut.ai.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibecut.ai.domain.repository.Project
import com.vibecut.ai.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeUiState(
    val projects: List<Project> = emptyList(),
    val aiCredits: Int = 5,
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = projectRepository.getProjects()
        .combine(MutableStateFlow(5)) { projects, credits ->
            HomeUiState(projects = projects, aiCredits = credits)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )
}
