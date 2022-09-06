package app.adi_random.dealscraper.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.flow.flow

class GalleryRepository(private val contentResolver: ContentResolver) {

    // TODO: Implement
    private fun getLastProcessedId():Long? = 44L

    fun getUnprocessedImagesUris() = flow {
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

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            // If the image id is different than the lastProcessedImageId, get the image uri and emit it
            // Else stop
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                if (id != getLastProcessedId()) {
                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    emit(contentUri)
                } else {
                    return@use
                }
            }
        }
        query?.close()
    }
}