package com.example.aperture.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.aperture.data.bdentities.PhotoEntity
import com.example.aperture.data.contracts.PhotoContract

@Dao
interface PhotoDao {

    @Query("SELECT * FROM ${PhotoContract.TABLE_NAME}")
    suspend fun getPhotos(): List<PhotoEntity>

    @Query("SELECT * FROM ${PhotoContract.TABLE_NAME} WHERE ${PhotoContract.Columns.ID} = :id")
    suspend fun getPhotoById(id: String): PhotoEntity?

    @Query("SELECT * FROM ${PhotoContract.TABLE_NAME} WHERE ${PhotoContract.Columns.COLLECTION_ID} = :id")
    suspend fun getCollectionPhotos(id: String?): List<PhotoEntity>

    @Insert
    suspend fun insertPhotos(list: List<PhotoEntity>)

    @Query("DELETE FROM ${PhotoContract.TABLE_NAME}")
    suspend fun deletePhotos()

    @Query("DELETE FROM ${PhotoContract.TABLE_NAME} WHERE ${PhotoContract.Columns.COLLECTION_ID} = :id")
    suspend fun deletePhotosByCollectionId(id: String?)
}