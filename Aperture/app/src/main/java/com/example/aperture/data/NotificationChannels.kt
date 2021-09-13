package com.example.aperture.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat

import com.example.aperture.util.haveO

object NotificationChannels {
    fun create(context: Context) {
        if (haveO()) {
            val name = "download"
            val descriptionText = ""
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            NotificationManagerCompat.from(context)
                    .createNotificationChannel(channel)
        }
    }

    const val CHANNEL_ID = "channel_id"
}