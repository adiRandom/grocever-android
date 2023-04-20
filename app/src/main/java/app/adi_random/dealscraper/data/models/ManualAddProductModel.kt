package app.adi_random.dealscraper.data.models

import app.adi_random.dealscraper.data.dto.product.CreateProductDto

open class ManualAddProductModel(
    val name: String,
    val unitPrice: Float,
    val quantity: Float,
    val unitName: String,
    val storeId: Int,
    val date: Long?
){
    open fun toDto() = CreateProductDto(
        ocrName = name,
        qty = quantity,
        unitPrice = unitPrice,
        unitName = unitName,
        storeId = storeId
    )
}
