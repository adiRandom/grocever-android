package app.adi_random.dealscraper.data.dto.product

import app.adi_random.dealscraper.data.models.UserProductInstalment

data class UserProductInstalmentDto(
    val id: Int,
    val qty: Float,
    val unitPrice: Float,
    val ocrName: String,
    val store: StoreMetadataDto,
    val unitName: String,
){
    fun toModel() = UserProductInstalment(
        qty = qty,
        unitPrice = unitPrice,
        ocrName = ocrName,
        store = store.toModel(),
        id = id,
        unitName = unitName
    )
}
