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
public final class BeatSyncAnalyzer_Factory implements Factory<BeatSyncAnalyzer> {
  @Override
  public BeatSyncAnalyzer get() {
    return newInstance();
  }

  public static BeatSyncAnalyzer_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static BeatSyncAnalyzer newInstance() {
    return new BeatSyncAnalyzer();
  }

  private static final class InstanceHolder {
    static final BeatSyncAnalyzer_Factory INSTANCE = new BeatSyncAnalyzer_Factory();
  }
}
