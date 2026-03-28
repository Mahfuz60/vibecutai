package com.vibecut.ai.data.video;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class FFmpegProcessor_Factory implements Factory<FFmpegProcessor> {
  private final Provider<Context> contextProvider;

  public FFmpegProcessor_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public FFmpegProcessor get() {
    return newInstance(contextProvider.get());
  }

  public static FFmpegProcessor_Factory create(javax.inject.Provider<Context> contextProvider) {
    return new FFmpegProcessor_Factory(Providers.asDaggerProvider(contextProvider));
  }

  public static FFmpegProcessor_Factory create(Provider<Context> contextProvider) {
    return new FFmpegProcessor_Factory(contextProvider);
  }

  public static FFmpegProcessor newInstance(Context context) {
    return new FFmpegProcessor(context);
  }
}
