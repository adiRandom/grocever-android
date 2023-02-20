package app.adi_random.dealscraper.data.dao

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.adi_random.dealscraper.data.entity.RetryUploadEntity

@Dao
abstract class RetryUploadDao {
    @Query("SELECT * FROM RetryUpload")
    abstract fun getRetryUploads(): List<RetryUploadEntity>

    @Insert
    abstract fun saveRetryUpload(retryUploadEntity: RetryUploadEntity)
    fun saveRetryUploads(uri: Uri) {
        saveRetryUpload(RetryUploadEntity( uri = uri.toString()))
    }

    @Query("DELETE FROM RetryUpload WHERE uri = :uri")
    abstract fun deleteRetryUpload(uri: String)

}