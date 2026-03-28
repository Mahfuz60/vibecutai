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
public final class CloneEffect_Factory implements Factory<CloneEffect> {
  @Override
  public CloneEffect get() {
    return newInstance();
  }

  public static CloneEffect_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CloneEffect newInstance() {
    return new CloneEffect();
  }

  private static final class InstanceHolder {
    static final CloneEffect_Factory INSTANCE = new CloneEffect_Factory();
  }
}
