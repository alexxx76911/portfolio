package com.example.aperture.data.bdentities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aperture.data.contracts.PhotoContract

@Entity(tableName = PhotoContract.TABLE_NAME)
data class PhotoEntity(
        @PrimaryKey
        @ColumnInfo(name = PhotoContract.Columns.ID)
        val id: String,
        @ColumnInfo(name = PhotoContract.Columns.IMAGE)
        val imagePath: String,
        @ColumnInfo(name = PhotoContract.Columns.AUTHOR_NAME)
        val authorName: String,
        @ColumnInfo(name = PhotoContract.Columns.AUTHOR_NICKNAME)
        val authorNickname: String,
        @ColumnInfo(name = PhotoContract.Columns.AUTHOR_AVATAR)
        val authorAvatar: ByteArray?,
        @ColumnInfo(name = PhotoContract.Columns.AUTHOR_BIO)
        val authorBio: String?,
        @ColumnInfo(name = PhotoContract.Columns.LIKED_BY_USER)
        val likedByUser: Int,
        @ColumnInfo(name = PhotoContract.Columns.LIKES)
        val likes: Int,
        @ColumnInfo(name = PhotoContract.Columns.URL)
        val url: String,
        @ColumnInfo(name = PhotoContract.Columns.COLLECTION_ID)
        val collectionId: String?
)
