package com.vibecut.ai.data.ai;

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
public final class AutoCaptionGenerator_Factory implements Factory<AutoCaptionGenerator> {
  private final Provider<Context> contextProvider;

  public AutoCaptionGenerator_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AutoCaptionGenerator get() {
    return newInstance(contextProvider.get());
  }

  public static AutoCaptionGenerator_Factory create(
      javax.inject.Provider<Context> contextProvider) {
    return new AutoCaptionGenerator_Factory(Providers.asDaggerProvider(contextProvider));
  }

  public static AutoCaptionGenerator_Factory create(Provider<Context> contextProvider) {
    return new AutoCaptionGenerator_Factory(contextProvider);
  }

  public static AutoCaptionGenerator newInstance(Context context) {
    return new AutoCaptionGenerator(context);
  }
}
