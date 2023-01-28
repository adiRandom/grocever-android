package app.adi_random.dealscraper.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.adi_random.dealscraper.data.models.UserProductInstalment

@Entity(tableName = "purchase_instalments")
data class PurchaseInstalmentEntity(
    @PrimaryKey
    val id: Int,
    val qty: Float,
    val unitPrice: Float,
    val ocrName: String,
    val storeName: String,
    val productName: String
){
    fun toModel() = UserProductInstalment(
        id = id,
        qty = qty,
        unitPrice = unitPrice,
        ocrName = ocrName,
        storeName = storeName
    )
}