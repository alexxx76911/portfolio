package com.example.homework

import android.content.Context
import androidx.room.Room


object DataBase {
    lateinit var instance: MoviesDB
        private set

    fun init(context: Context) {
        instance = Room.databaseBuilder(context, MoviesDB::class.java, MoviesDB.DB_NAME).build()
    }
}