package com.example.aperture.data.bdentities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aperture.data.contracts.PhotoCollectionContract

@Entity(tableName = PhotoCollectionContract.TABLE_NAME)
data class PhotoCollectionEntity(
        @PrimaryKey
        @ColumnInfo(name = PhotoCollectionContract.Columns.ID)
        val id: String,
        @ColumnInfo(name = PhotoCollectionContract.Columns.NAME)
        val name: String,
        @ColumnInfo(name = PhotoCollectionContract.Columns.PHOTO_COUNT)
        val photoCount: Int,
        @ColumnInfo(name = PhotoCollectionContract.Columns.COVER_PHOTO)
        val coverPhotoPath: String,
        @ColumnInfo(name = PhotoCollectionContract.Columns.AUTHOR_NAME)
        val authorName: String,
        @ColumnInfo(name = PhotoCollectionContract.Columns.AUTHOR_NICKNAME)
        val authorNickname: String,
        @ColumnInfo(name = PhotoCollectionContract.Columns.AUTHOR_AVATAR)
        val authorAvatar: ByteArray?,
        @ColumnInfo(name = PhotoCollectionContract.Columns.DESCRIPTION)
        val description: String?
)
