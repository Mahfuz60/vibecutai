package com.vibecut.ai.data.ai;

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
public final class BackgroundRemovalProcessor_Factory implements Factory<BackgroundRemovalProcessor> {
  @Override
  public BackgroundRemovalProcessor get() {
    return newInstance();
  }

  public static BackgroundRemovalProcessor_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static BackgroundRemovalProcessor newInstance() {
    return new BackgroundRemovalProcessor();
  }

  private static final class InstanceHolder {
    static final BackgroundRemovalProcessor_Factory INSTANCE = new BackgroundRemovalProcessor_Factory();
  }
}
