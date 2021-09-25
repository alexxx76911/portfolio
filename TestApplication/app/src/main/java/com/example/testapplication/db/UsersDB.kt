package com.example.testapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testapplication.Person

@Database(entities = [Person::class], version = UsersDB.DB_VERSION)
abstract class UsersDB : RoomDatabase() {
    abstract fun userDao(): UserDao


    companion object {
        const val DB_NAME = "usersDb"
        const val DB_VERSION = 1
    }
}