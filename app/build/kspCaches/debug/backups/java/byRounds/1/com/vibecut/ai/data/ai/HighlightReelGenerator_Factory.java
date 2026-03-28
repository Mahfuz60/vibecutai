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
public final class HighlightReelGenerator_Factory implements Factory<HighlightReelGenerator> {
  @Override
  public HighlightReelGenerator get() {
    return newInstance();
  }

  public static HighlightReelGenerator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static HighlightReelGenerator newInstance() {
    return new HighlightReelGenerator();
  }

  private static final class InstanceHolder {
    static final HighlightReelGenerator_Factory INSTANCE = new HighlightReelGenerator_Factory();
  }
}
