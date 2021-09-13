package com.example.aperture.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.aperture.data.bdentities.PhotoCollectionEntity
import com.example.aperture.data.contracts.PhotoCollectionContract

@Dao
interface PhotoCollectionDao {

    @Query("SELECT * FROM ${PhotoCollectionContract.TABLE_NAME}")
    suspend fun getPhotoCollections(): List<PhotoCollectionEntity>

    @Query("SELECT * FROM ${PhotoCollectionContract.TABLE_NAME} WHERE ${PhotoCollectionContract.Columns.ID} = :id")
    suspend fun getCollectionById(id: String): PhotoCollectionEntity


    @Insert
    suspend fun insertPhotoCollections(list: List<PhotoCollectionEntity>)

    @Query("DELETE FROM ${PhotoCollectionContract.TABLE_NAME}")
    suspend fun deleteCollections()
}