package com.example.aperture.data.contracts


object PhotoCollectionContract {
    const val TABLE_NAME = "photo_collections"

    object Columns {
        const val ID = "id"
        const val NAME = "name"
        const val COVER_PHOTO = "cover_photo"
        const val PHOTO_COUNT = "photo_count"
        const val AUTHOR_NAME = "author_name"
        const val AUTHOR_NICKNAME = "author_nickname"
        const val AUTHOR_AVATAR = "author_avatar"
        const val DESCRIPTION = "description"

    }
}