package com.example.aperture.data.db

import android.content.Context
import androidx.room.Room

object DataBase {
    lateinit var photoDb: PhotoDb
        private set

    fun init(context: Context) {
        photoDb = Room.databaseBuilder(context, PhotoDb::class.java, PhotoDb.DB_NAME).build()

    }
}