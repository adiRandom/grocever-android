package app.adi_random.dealscraper.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GalleryRepository(private val contentResolver: ContentResolver?) {

    // TODO: Implement
    private fun getLastProcessedId():Long? = null

    suspend fun getUnprocessedImagesUris() = suspendCoroutine<List<Uri>> { continuation ->
        if(contentResolver == null) {
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
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            // If the image id is different than the lastProcessedImageId, get the image uri and emit it
            // Else stop
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                if (id != getLastProcessedId()) {
                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    uriList.add(contentUri)
                } else {
                    return@use
                }
            }
        }
        query?.close()

        continuation.resume(uriList)
    }
}