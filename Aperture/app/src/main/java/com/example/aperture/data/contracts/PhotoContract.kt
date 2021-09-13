package com.example.aperture.data.contracts

object PhotoContract {
    const val TABLE_NAME = "photos"

    object Columns {
        const val ID = "id"
        const val IMAGE = "image"
        const val AUTHOR_NAME = "author_name"
        const val AUTHOR_NICKNAME = "author_nickname"
        const val AUTHOR_AVATAR = "author_avatar"
        const val AUTHOR_BIO = "author_bio"
        const val LIKED_BY_USER = "liked_by_user"
        const val LIKES = "likes"
        const val URL = "url"
        const val COLLECTION_ID = "collection_id"

    }
}