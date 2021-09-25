package com.example.testapplication.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testapplication.Person
import com.example.testapplication.PersonContract

@Dao
interface UserDao {
    @Query("SELECT * FROM ${PersonContract.TABLE_NAME}")
    fun getUsers(): List<Person>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(list: List<Person>)
}