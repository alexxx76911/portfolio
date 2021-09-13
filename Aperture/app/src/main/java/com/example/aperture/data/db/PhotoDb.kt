package com.example.aperture.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aperture.data.bdentities.PhotoCollectionEntity
import com.example.aperture.data.bdentities.PhotoEntity
import com.example.aperture.data.daos.PhotoCollectionDao
import com.example.aperture.data.daos.PhotoDao

@Database(entities = [PhotoEntity::class, PhotoCollectionEntity::class], version = PhotoDb.DB_VERSION)
abstract class PhotoDb : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun photoCollectionDao(): PhotoCollectionDao


    companion object {
        const val DB_NAME = "photoDb"
        const val DB_VERSION = 1
    }
}