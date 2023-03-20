package app.adi_random.dealscraper.data.dto.product

data class CreateProductDto(
    val ocrName: String,
    val qty: Float,
    val unitPrice: Float,
    val unitName: String,
    val storeId: Int
)
