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
public final class ChromakeyProcessor_Factory implements Factory<ChromakeyProcessor> {
  @Override
  public ChromakeyProcessor get() {
    return newInstance();
  }

  public static ChromakeyProcessor_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ChromakeyProcessor newInstance() {
    return new ChromakeyProcessor();
  }

  private static final class InstanceHolder {
    static final ChromakeyProcessor_Factory INSTANCE = new ChromakeyProcessor_Factory();
  }
}
