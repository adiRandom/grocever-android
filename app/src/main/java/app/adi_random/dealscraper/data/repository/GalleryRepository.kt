package app.adi_random.dealscraper.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import app.adi_random.dealscraper.data.dao.RetryUploadDao
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GalleryRepository(
    private val contentResolver: ContentResolver?,
    private val retryUploadDao: RetryUploadDao,
    private val preferencesRepository: PreferencesRepository
) {
    fun saveImageForRetry(uri: Uri) {
        retryUploadDao.saveRetryUploads(uri)
    }

    fun getRetryUploads(): List<Uri> {
        return retryUploadDao.getRetryUploads().map { Uri.parse(it.uri) }
    }

    suspend fun getUnprocessedImagesUris() = suspendCoroutine<List<Uri>> { continuation ->
        if (contentResolver == null) {
            continuation.resume(emptyList())
            return@suspendCoroutine
        }

        val collectionUri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED,
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val query = contentResolver.query(collectionUri, projection, null, null, sortOrder)

        val uriList = mutableListOf<Uri>()
        var newestId: Long? = null
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            // If the image id is different than the lastProcessedImageId, get the image uri and emit it
            // Else stop
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)

                if(newestId == null){
                    newestId = id
                }

                if (id != preferencesRepository.getLastCheckedImageId()) {
                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    uriList.add(contentUri)
                } else {
                    return@use
                }
            }
        }

        if(newestId != null){
            preferencesRepository.saveLastCheckedImageId(newestId!!)
        }

        query?.close()

        continuation.resume(uriList)
    }

    fun removeRetryUpload(uri: Uri) {
        retryUploadDao.deleteRetryUpload(uri.toString())
    }
}