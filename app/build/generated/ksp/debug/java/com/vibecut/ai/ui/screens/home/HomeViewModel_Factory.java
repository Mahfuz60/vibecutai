package com.vibecut.ai.ui.screens.home;

import com.vibecut.ai.domain.repository.ProjectRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<ProjectRepository> projectRepositoryProvider;

  public HomeViewModel_Factory(Provider<ProjectRepository> projectRepositoryProvider) {
    this.projectRepositoryProvider = projectRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(projectRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      javax.inject.Provider<ProjectRepository> projectRepositoryProvider) {
    return new HomeViewModel_Factory(Providers.asDaggerProvider(projectRepositoryProvider));
  }

  public static HomeViewModel_Factory create(
      Provider<ProjectRepository> projectRepositoryProvider) {
    return new HomeViewModel_Factory(projectRepositoryProvider);
  }

  public static HomeViewModel newInstance(ProjectRepository projectRepository) {
    return new HomeViewModel(projectRepository);
  }
}
