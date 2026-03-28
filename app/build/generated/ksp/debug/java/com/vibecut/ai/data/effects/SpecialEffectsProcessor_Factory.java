package com.vibecut.ai.data.effects;

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
public final class SpecialEffectsProcessor_Factory implements Factory<SpecialEffectsProcessor> {
  @Override
  public SpecialEffectsProcessor get() {
    return newInstance();
  }

  public static SpecialEffectsProcessor_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SpecialEffectsProcessor newInstance() {
    return new SpecialEffectsProcessor();
  }

  private static final class InstanceHolder {
    static final SpecialEffectsProcessor_Factory INSTANCE = new SpecialEffectsProcessor_Factory();
  }
}
