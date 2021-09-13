package com.example.aperture

import android.app.Application
import com.example.aperture.data.db.DataBase
import com.example.aperture.data.NotificationChannels

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DataBase.init(this)
        NotificationChannels.create(this)
    }

}