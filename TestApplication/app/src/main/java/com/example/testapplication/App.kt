package com.example.testapplication

import android.app.Application
import com.example.testapplication.db.DataBase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DataBase.init(this)
    }
}