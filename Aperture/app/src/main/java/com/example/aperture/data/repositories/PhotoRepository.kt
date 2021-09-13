package com.example.aperture.data.repositories

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

import androidx.lifecycle.LiveData
import androidx.work.*

import com.example.aperture.data.bdentities.PhotoCollectionEntity
import com.example.aperture.data.bdentities.PhotoEntity
import com.example.aperture.data.db.DataBase
import com.example.aperture.data.jsonclasses.*
import com.example.aperture.network.NetWork
import com.example.aperture.util.DownloadWorker
import com.example.aperture.util.haveQ
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.lang.Exception

class PhotoRepository(private val context: Context) {
    private val photoDao = DataBase.photoDb.photoDao()
    private val photoCollectionDao = DataBase.photoDb.photoCollectionDao()


    suspend fun getPhotos(page: Int): List<Photo>? {
        return try {
            NetWork.photoApi.getPhotos(page)
        } catch (e: Throwable) {
            null
        }
    }

    suspend fun getUserPhotos(userName: String, page: Int): List<Photo>? {
        return try {
            NetWork.photoApi.getUserPhotos(userName, page)
        } catch (e: Throwable) {
            null
        }
    }

    suspend fun getUserLikedPhotos(userName: String, page: Int): List<Photo>? {
        return try {
            NetWork.photoApi.getUserLikedPhotos(userName, page)
        } catch (e: Throwable) {
            null
        }
    }


    suspend fun getCollectionPhotos(id: String, page: Int): List<Photo>? {
        return try {
            NetWork.photoApi.getCollectionPhotos(id, page)
        } catch (e: Throwable) {
            null
        }
    }


    suspend fun getCollections(page: Int): List<PhotoCollection>? {
        return try {
            NetWork.photoApi.getCollections(page)
        } catch (e: Throwable) {
            null
        }
    }

    suspend fun getUserCollections(userName: String, page: Int): List<PhotoCollection>? {
        return try {
            NetWork.photoApi.getUserCollections(userName, page)
        } catch (e: Throwable) {
            null
        }
    }

    suspend fun getMyProfile(): Profile? {
        return try {
            NetWork.photoApi.getProfile()
        } catch (e: Throwable) {
            null
        }
    }

    suspend fun getProfileByUrl(url: String): Author? {
        return try {
            NetWork.photoApi.getProfileByUrl(url)
        } catch (e: Throwable) {
            null
        }
    }

    suspend fun getPhotoById(id: String): Photo? {
        return try {
            NetWork.photoApi.getPhotoById(id)
        } catch (e: Exception) {
            null
        }

    }

    suspend fun getCollectionById(id: String): PhotoCollection? {
        return try {
            NetWork.photoApi.getCollectionById(id)
        } catch (e: Throwable) {
            null
        }
    }

    suspend fun searchPhotos(query: String): SearchResult? {
        return try {
            NetWork.photoApi.searchPhotos(query)
        } catch (e: Throwable) {
            null
        }
    }

    suspend fun likePhoto(id: String): PhotoWrapper? {
        return try {
            NetWork.photoApi.likePhoto(id)
        } catch (e: Throwable) {
            null
        }
    }

    suspend fun unlikePhoto(id: String): PhotoWrapper? {
        return try {
            NetWork.photoApi.unlikePhoto(id)
        } catch (e: Throwable) {
            null
        }
    }


    suspend fun getCollectionFromBd(id: String): PhotoCollectionEntity? {
        return try {
            photoCollectionDao.getCollectionById(id)
        } catch (e: Throwable) {
            null
        }
    }

    suspend fun savePhotosToBd(list: List<Photo>, collectionId: String?) {
        try {
            var fileCount = 0
            photoDao.deletePhotosByCollectionId(collectionId)
            val listToSave = list.map {
                fileCount++
                convertPhotoToPhotoEntity(
                    it,
                    "${collectionId.orEmpty()}photo$fileCount",
                    collectionId
                )
            }
            photoDao.insertPhotos(listToSave)

        } catch (e: Throwable) {
            photoDao.deletePhotos()
        }
    }


    suspend fun getCollectionPhotosFromBd(collectionId: String?): List<PhotoEntity>? {
        return try {
            photoDao.getCollectionPhotos(collectionId)
        } catch (e: Throwable) {
            null
        }

    }

    suspend fun savePhotoCollectionsToBd(list: List<PhotoCollection>) {
        try {
            var fileCount = 0
            photoCollectionDao.deleteCollections()
            val listToSave = list.map {
                fileCount++
                convertToPhotoCollectionEntity(it, "collection$fileCount")
            }
            photoCollectionDao.insertPhotoCollections(listToSave)

        } catch (e: Throwable) {
            photoCollectionDao.deleteCollections()
        }

    }

    suspend fun getCollectionsFromBd(): List<PhotoCollectionEntity>? {
        return try {
            photoCollectionDao.getPhotoCollections()
        } catch (e: Throwable) {
            null
        }
    }


    fun getShareIntentById(id: String): Intent {
        val uri = Uri.withAppendedPath(Uri.parse("https://unsplash.com/photos/"), id)
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, uri.toString())
            type = "text/plain"
        }
    }

    private suspend fun convertPhotoToPhotoEntity(
        photo: Photo,
        fileName: String,
        collectionId: String?
    ): PhotoEntity {
        return PhotoEntity(
            id = photo.id,
            imagePath = downLoadImageToInternalSpace(fileName, photo.urls.raw),
            authorName = photo.user.name.orEmpty(),
            authorNickname = photo.user.username,
            authorAvatar = if (photo.user.profile_image != null) {
                NetWork.photoApi.downLoadImage(photo.user.profile_image.small).byteStream()
                    .use { it.readBytes() }
            } else {
                null
            },
            authorBio = photo.user.bio,
            if (photo.liked_by_user) {
                1
            } else {
                0
            },
            likes = photo.likes,
            url = photo.urls.raw,
            collectionId = collectionId.orEmpty()
        )
    }

    private suspend fun convertToPhotoCollectionEntity(
        collection: PhotoCollection,
        fileName: String
    ): PhotoCollectionEntity {
        return PhotoCollectionEntity(
            id = collection.id,
            name = collection.title,
            photoCount = collection.total_photos,
            coverPhotoPath = downLoadImageToInternalSpace(
                fileName,
                collection.cover_photo.urls.raw
            ),
            authorName = collection.user.name.orEmpty(),
            authorNickname = collection.user.username,
            authorAvatar = if (collection.user.profile_image != null) {
                NetWork.photoApi.downLoadImage(collection.user.profile_image.small).byteStream()
                    .use { it.readBytes() }
            } else {
                null
            },
            description = collection.description
        )

    }

    private suspend fun downLoadImageToInternalSpace(name: String, url: String): String {
        val dir = context.filesDir
        val file = File(dir, name)
        downLoadFile(file.outputStream().buffered(), url)
        return file.path
    }

    suspend fun saveImageToMediaStore(name: String, url: String, id: String): Uri {
        return withContext(Dispatchers.IO) {
            val photoUri = saveImageDetails(name)
            try {
                downLoadFile(context.contentResolver.openOutputStream(photoUri)?.buffered(), url)
                makePhotoVisible(photoUri)
                NetWork.photoApi.trackDownload(id)

            } catch (e: Exception) {
                context.contentResolver.delete(photoUri, null, null)
                throw e
            }
            photoUri
        }

    }

    private fun saveImageDetails(name: String): Uri {
        val photosVolumeUri = if (haveQ()) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (haveQ()) {
                put(MediaStore.Video.Media.IS_PENDING, 1)
            }
        }

        return context.contentResolver.insert(photosVolumeUri, contentValues)!!

    }

    private fun makePhotoVisible(uri: Uri) {
        if (haveQ().not()) return
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.IS_PENDING, 0)
        }

        context.contentResolver.update(uri, contentValues, null, null)
    }

    private suspend fun downLoadFile(outputStream: BufferedOutputStream?, url: String) {
        if (outputStream == null) {
            return
        }
        try {
            outputStream.use { output ->
                NetWork.photoApi.downLoadImage(url).byteStream().buffered().use { input ->
                    input.copyTo(output)
                }
            }
        } catch (e: Throwable) {
            throw e
        }
    }

    fun startDownloadWork(fileName: String, url: String, id: String): LiveData<WorkInfo> {
        val workData = workDataOf(
            DownloadWorker.FILE_NAME_KEY to fileName,
            DownloadWorker.FILE_URL_KEY to url,
            DownloadWorker.PHOTO_ID_KEY to id
        )
        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(workConstraints)
            .setInputData(workData)
            .build()

        WorkManager.getInstance(context)
            .enqueue(workRequest)

        return WorkManager.getInstance(context).getWorkInfoByIdLiveData(workRequest.id)

    }

    suspend fun deleteAllDataFromBd() {
        val allPhotos = photoDao.getPhotos()
        val allCollections = photoCollectionDao.getPhotoCollections()
        allPhotos.forEach { File(it.imagePath).delete() }
        allCollections.forEach { File(it.coverPhotoPath).delete() }
        photoDao.deletePhotos()
        photoCollectionDao.deleteCollections()
    }


}