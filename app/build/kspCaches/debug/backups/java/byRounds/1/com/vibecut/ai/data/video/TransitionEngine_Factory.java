package com.vibecut.ai.data.video;

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
public final class TransitionEngine_Factory implements Factory<TransitionEngine> {
  @Override
  public TransitionEngine get() {
    return newInstance();
  }

  public static TransitionEngine_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TransitionEngine newInstance() {
    return new TransitionEngine();
  }

  private static final class InstanceHolder {
    static final TransitionEngine_Factory INSTANCE = new TransitionEngine_Factory();
  }
}
