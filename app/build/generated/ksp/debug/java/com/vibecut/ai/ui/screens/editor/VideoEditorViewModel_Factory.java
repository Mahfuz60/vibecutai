package com.vibecut.ai.ui.screens.editor;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
  @Override
  public VideoEditorViewModel get() {
    return newInstance();
  }

  public static VideoEditorViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static VideoEditorViewModel newInstance() {
    return new VideoEditorViewModel();
  }

  private static final class InstanceHolder {
    static final VideoEditorViewModel_Factory INSTANCE = new VideoEditorViewModel_Factory();
  }
}
