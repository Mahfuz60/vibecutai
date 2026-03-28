package com.vibecut.ai.domain.repository

import kotlinx.coroutines.flow.Flow

data class Project(
    val id: String,
    val name: String,
    val videoUri: String,
    val lastModified: Long
)

interface ProjectRepository {
    fun getProjects(): Flow<List<Project>>
    suspend fun getProjectById(id: String): Project?
    suspend fun saveProject(project: Project)
    suspend fun deleteProject(id: String)
}
