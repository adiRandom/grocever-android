package app.adi_random.dealscraper.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.adi_random.dealscraper.data.models.StoreMetadataModel
import app.adi_random.dealscraper.data.models.UserProductInstalment

@Entity(tableName = "purchase_instalments")
data class PurchaseInstalmentEntity(
    @PrimaryKey
    val id: Int,
    val qty: Float,
    val unitPrice: Float,
    val ocrName: String,
    @Embedded
    val store: StoreMetadataModel,
    val productName: String,
    val unitName: String,
    val date: Long
){
    fun toModel() = UserProductInstalment(
        id = id,
        qty = qty,
        unitPrice = unitPrice,
        ocrName = ocrName,
        store = store,
        unitName = unitName,
        date = date
    )
}