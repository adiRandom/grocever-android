package app.adi_random.dealscraper.data.models

import app.adi_random.dealscraper.data.dto.product.CreateProductDot

data class ManualAddProductModel(
    val name: String,
    val unitPrice: Float,
    val quantity: Float,
    val unitName: String,
    val storeId: Int
){
    fun toDto() = CreateProductDot(
        ocrName = name,
        qty = quantity,
        unitPrice = unitPrice,
        unitName = unitName,
        storeId = storeId
    )
}
