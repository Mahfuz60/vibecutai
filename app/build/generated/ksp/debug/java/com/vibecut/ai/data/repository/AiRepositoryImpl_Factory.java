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
public final class AiRepositoryImpl_Factory implements Factory<AiRepositoryImpl> {
  @Override
  public AiRepositoryImpl get() {
    return newInstance();
  }

  public static AiRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AiRepositoryImpl newInstance() {
    return new AiRepositoryImpl();
  }

  private static final class InstanceHolder {
    static final AiRepositoryImpl_Factory INSTANCE = new AiRepositoryImpl_Factory();
  }
}
