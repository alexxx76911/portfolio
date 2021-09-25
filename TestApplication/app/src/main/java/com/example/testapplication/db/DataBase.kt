package com.example.testapplication.db

import android.content.Context
import androidx.room.Room

object DataBase {
    lateinit var usersDb: UsersDB
        private set

    fun init(context: Context) {
        usersDb = Room.databaseBuilder(context, UsersDB::class.java, UsersDB.DB_NAME).build()
    }
}