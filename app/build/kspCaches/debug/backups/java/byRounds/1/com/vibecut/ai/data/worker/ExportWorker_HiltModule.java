package com.vibecut.ai.data.worker;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = ExportWorker.class
)
public interface ExportWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.vibecut.ai.data.worker.ExportWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(ExportWorker_AssistedFactory factory);
}
