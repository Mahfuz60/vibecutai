package com.vibecut.ai.data.ads;

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
public final class AdMobManager_Factory implements Factory<AdMobManager> {
  private final Provider<Context> contextProvider;

  public AdMobManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AdMobManager get() {
    return newInstance(contextProvider.get());
  }

  public static AdMobManager_Factory create(javax.inject.Provider<Context> contextProvider) {
    return new AdMobManager_Factory(Providers.asDaggerProvider(contextProvider));
  }

  public static AdMobManager_Factory create(Provider<Context> contextProvider) {
    return new AdMobManager_Factory(contextProvider);
  }

  public static AdMobManager newInstance(Context context) {
    return new AdMobManager(context);
  }
}
