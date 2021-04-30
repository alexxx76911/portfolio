package com.example.homework

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DataBase.init(this)
    }
}