package app.adi_random.dealscraper.data.models

import app.adi_random.dealscraper.data.dto.product.UserProductInstalmentDto
import app.adi_random.dealscraper.data.entity.ProductEntity

data class ProductModel(
    val name: String,
    val bestPrice: Float,
    val purchaseInstalments: List<UserProductInstalment>,
    val bestStore: StoreMetadataModel
){
    fun toEntity() = ProductEntity(
        name = name,
        bestPrice = bestPrice,
        bestStore = bestStore
    )
}