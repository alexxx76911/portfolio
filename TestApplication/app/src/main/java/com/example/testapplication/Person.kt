package com.example.testapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = PersonContract.TABLE_NAME)
data class Person(
    @PrimaryKey
    @ColumnInfo(name = PersonContract.Columns.ID)
    val id: Int,
    @ColumnInfo(name = PersonContract.Columns.FIRST_NAME)
    val first_name: String,
    @ColumnInfo(name = PersonContract.Columns.LAST_NAME)
    val last_name: String,
    @ColumnInfo(name = PersonContract.Columns.EMAIL)
    val email: String,
    @ColumnInfo(name = PersonContract.Columns.AVATAR)
    val avatar: String
)
