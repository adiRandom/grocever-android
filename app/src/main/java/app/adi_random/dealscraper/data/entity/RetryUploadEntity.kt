package app.adi_random.dealscraper.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RetryUpload")
data class RetryUploadEntity(@PrimaryKey(autoGenerate = true) val id: Int = 0, val uri: String)
