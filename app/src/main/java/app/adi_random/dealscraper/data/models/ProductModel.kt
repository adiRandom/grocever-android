package app.adi_random.dealscraper.data.models

import app.adi_random.dealscraper.data.dto.ProductDto

data class ProductModel(
    val id: Int,
    val name: String,
    val ocrName: String,
    val price: Float,
    val bestPrice: Float,
    val url: String,
    val store: String,
){
    constructor(dto: ProductDto) : this(
        dto.id,
        dto.name,
        dto.ocrName,
        dto.price,
        dto.bestPrice,
        dto.url,
        dto.store
    )
}
