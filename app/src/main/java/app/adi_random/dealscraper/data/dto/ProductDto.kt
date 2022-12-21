package app.adi_random.dealscraper.data.dto

data class ProductDto(
    val id:Int,
    val name: String,
    val ocrName: String,
    val price: Float,
    val bestPrice: Float,
    val url: String,
    val store: String,
)
