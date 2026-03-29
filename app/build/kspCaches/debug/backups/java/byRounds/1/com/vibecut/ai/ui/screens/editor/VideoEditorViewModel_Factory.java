package com.vibecut.ai.ui.screens.editor;

import androidx.lifecycle.SavedStateHandle;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class VideoEditorViewModel_Factory implements Factory<VideoEditorViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public VideoEditorViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public VideoEditorViewModel get() {
    return newInstance(savedStateHandleProvider.get());
  }

  public static VideoEditorViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider) {
    return new VideoEditorViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider));
  }

  public static VideoEditorViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new VideoEditorViewModel_Factory(savedStateHandleProvider);
  }

  public static VideoEditorViewModel newInstance(SavedStateHandle savedStateHandle) {
    return new VideoEditorViewModel(savedStateHandle);
  }
}
