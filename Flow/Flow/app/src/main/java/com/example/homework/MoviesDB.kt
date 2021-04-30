package com.example.homework

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Movie::class], version = MoviesDB.DB_VERSION)
abstract class MoviesDB : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "moviesDB"
    }
}