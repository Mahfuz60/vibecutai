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
public final class FilterProcessor_Factory implements Factory<FilterProcessor> {
  @Override
  public FilterProcessor get() {
    return newInstance();
  }

  public static FilterProcessor_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FilterProcessor newInstance() {
    return new FilterProcessor();
  }

  private static final class InstanceHolder {
    static final FilterProcessor_Factory INSTANCE = new FilterProcessor_Factory();
  }
}
