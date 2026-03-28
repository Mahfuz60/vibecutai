package com.vibecut.ai.di

import com.vibecut.ai.data.repository.AiRepositoryImpl
import com.vibecut.ai.data.repository.AuthRepositoryImpl
import com.vibecut.ai.data.repository.ProjectRepositoryImpl
import com.vibecut.ai.domain.repository.AiRepository
import com.vibecut.ai.domain.repository.AuthRepository
import com.vibecut.ai.domain.repository.ProjectRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProjectRepository(
        impl: ProjectRepositoryImpl
    ): ProjectRepository

    @Binds
    @Singleton
    abstract fun bindAiRepository(
        impl: AiRepositoryImpl
    ): AiRepository
}