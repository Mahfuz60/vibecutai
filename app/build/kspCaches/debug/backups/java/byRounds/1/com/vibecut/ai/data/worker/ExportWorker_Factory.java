package com.vibecut.ai.data.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.vibecut.ai.data.video.FFmpegProcessor;
import dagger.internal.DaggerGenerated;
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
public final class ExportWorker_Factory {
  private final Provider<FFmpegProcessor> ffmpegProcessorProvider;

  public ExportWorker_Factory(Provider<FFmpegProcessor> ffmpegProcessorProvider) {
    this.ffmpegProcessorProvider = ffmpegProcessorProvider;
  }

  public ExportWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, ffmpegProcessorProvider.get());
  }

  public static ExportWorker_Factory create(
      javax.inject.Provider<FFmpegProcessor> ffmpegProcessorProvider) {
    return new ExportWorker_Factory(Providers.asDaggerProvider(ffmpegProcessorProvider));
  }

  public static ExportWorker_Factory create(Provider<FFmpegProcessor> ffmpegProcessorProvider) {
    return new ExportWorker_Factory(ffmpegProcessorProvider);
  }

  public static ExportWorker newInstance(Context context, WorkerParameters workerParams,
      FFmpegProcessor ffmpegProcessor) {
    return new ExportWorker(context, workerParams, ffmpegProcessor);
  }
}
