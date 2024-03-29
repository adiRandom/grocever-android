package app.adi_random.dealscraper.data.dto.product

import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.data.models.StoreMetadataModel
import app.adi_random.dealscraper.data.models.UserProductInstalment

data class UserProductDto(
    val id: Int,
    val name: String,
    val bestPrice: Float,
    val purchaseInstalments: List<UserProductInstalmentDto>,
    val bestStoreId: Int,
    val bestStoreName: String,
    val bestStoreUrl: String,
    val bestProductUrl: String,
    val imageUrl: String
){
    fun toModel() = ProductModel(
        id = id,
        name = name,
        bestPrice = bestPrice,
        purchaseInstalments = purchaseInstalments.map { it.toModel() },
        bestStore = StoreMetadataModel(
            id = bestStoreId,
            name = bestStoreName,
            url = bestStoreUrl
        ),
        url = bestProductUrl,
        imageUrl = imageUrl
    )
}