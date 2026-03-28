package com.vibecut.ai.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.vibecut.ai.data.video.FFmpegProcessor
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class ExportWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val ffmpegProcessor: FFmpegProcessor
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_VIDEO_PATH = "video_path"
        const val KEY_IS_4K = "is_4k"
        const val KEY_OUTPUT_PATH = "output_path"
        const val CHANNEL_ID = "vibecut_export"

        fun buildRequest(videoPath: String, is4K: Boolean): OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<ExportWorker>()
                .setInputData(
                    workDataOf(
                        KEY_VIDEO_PATH to videoPath,
                        KEY_IS_4K to is4K
                    )
                )
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val videoPath = inputData.getString(KEY_VIDEO_PATH) ?: return@withContext Result.failure()
        val is4K = inputData.getBoolean(KEY_IS_4K, false)

        createNotificationChannel()
        setForeground(createForegroundInfo("Exporting video..."))

        val exportResult = ffmpegProcessor.exportHD(videoPath, is4K)
        exportResult.fold(
            onSuccess = { outputPath ->
                Result.success(workDataOf(KEY_OUTPUT_PATH to outputPath))
            },
            onFailure = {
                Result.failure(workDataOf("error" to (it.message ?: "Export failed")))
            }
        )
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("VibeCut AI")
            .setContentText(progress)
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .setOngoing(true)
            .setProgress(100, 0, true)
            .build()
        return ForegroundInfo(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Video Export", NotificationManager.IMPORTANCE_LOW)
            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}