package app.adi_random.dealscraper.data.entity

import android.net.Uri
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.adi_random.dealscraper.data.models.StoreMetadataModel

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val bestPrice: Float,
    @Embedded
    val bestStore: StoreMetadataModel,
    val url: String,
    val imageUrl: String
)
