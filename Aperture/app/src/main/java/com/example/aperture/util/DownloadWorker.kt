package com.example.aperture.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.aperture.R
import com.example.aperture.data.AppStatus
import com.example.aperture.data.NotificationChannels
import com.example.aperture.data.repositories.PhotoRepository

class DownloadWorker(private val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val photoRepository = PhotoRepository(context)
    override suspend fun doWork(): Result {
        return try {
            val fileName = inputData.getString(FILE_NAME_KEY).orEmpty()
            val fileUrl = inputData.getString(FILE_URL_KEY).orEmpty()
            val photoId = inputData.getString(PHOTO_ID_KEY).orEmpty()
            val fileUri = photoRepository.saveImageToMediaStore(fileName, fileUrl, photoId)

            if (AppStatus.isAppVisible.not()) {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = fileUri
                }
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)


                showNotification(pendingIntent)
            }

            Result.success(workDataOf(FILE_CONTENT_URI to fileUri.toString()))
        } catch (e: Throwable) {
            Result.failure()
        }


    }

    private fun showNotification(intent: PendingIntent) {
        val notification = NotificationCompat.Builder(context, NotificationChannels.CHANNEL_ID)
                .setContentTitle(context.getString(R.string.snackBarDownloadSuccess))
                .setContentText(context.getString(R.string.snackBarOpenAction))
                .setSmallIcon(R.drawable.ic_baseline_image_24)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)

    }


    companion object {
        const val PHOTO_ID_KEY = "photo_id"
        const val FILE_URL_KEY = "file_url"
        const val FILE_NAME_KEY = "file_name"
        const val FILE_CONTENT_URI = "file_content_uri"
        const val NOTIFICATION_ID = 1
    }

}