package app.adi_random.dealscraper.data.models

import app.adi_random.dealscraper.data.entity.PurchaseInstalmentEntity

data class UserProductInstalment(
    val id: Int,
    val qty: Float,
    val unitPrice: Float,
    val ocrName: String,
    val store: StoreMetadataModel,
    val unitName: String,
){
    fun toEntity(productName: String) = PurchaseInstalmentEntity(
        qty = qty,
        unitPrice = unitPrice,
        ocrName = ocrName,
        store = store,
        productName = productName,
        id = id,
        unitName = unitName
    )
}
