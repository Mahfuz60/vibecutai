package com.vibecut.ai.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ProjectRepositoryImpl_Factory implements Factory<ProjectRepositoryImpl> {
  @Override
  public ProjectRepositoryImpl get() {
    return newInstance();
  }

  public static ProjectRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ProjectRepositoryImpl newInstance() {
    return new ProjectRepositoryImpl();
  }

  private static final class InstanceHolder {
    static final ProjectRepositoryImpl_Factory INSTANCE = new ProjectRepositoryImpl_Factory();
  }
}
