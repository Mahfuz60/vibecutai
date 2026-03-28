package com.vibecut.ai.data.repository

import com.vibecut.ai.domain.repository.Project
import com.vibecut.ai.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryImpl @Inject constructor() : ProjectRepository {
    private val _projects = MutableStateFlow<List<Project>>(emptyList())

    override fun getProjects(): Flow<List<Project>> = _projects.asStateFlow()

    override suspend fun getProjectById(id: String): Project? {
        return _projects.value.find { it.id == id }
    }

    override suspend fun saveProject(project: Project) {
        val currentList = _projects.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == project.id }
        if (index != -1) {
            currentList[index] = project
        } else {
            currentList.add(project)
        }
        _projects.value = currentList
    }

    override suspend fun deleteProject(id: String) {
        _projects.value = _projects.value.filter { it.id != id }
    }
}
