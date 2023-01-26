package app.adi_random.dealscraper.data.dto.product

import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.data.models.StoreMetadataModel

data class UserProductDto(
    val id: Int,
    val name: String,
    val ocrName: String,
    val bestPrice: Float,
    val purchaseInstalments: List<UserProductInstalmentDto>,
    val unitName: String,
    val bestStoreId: Int,
    val bestStoreName: String,
    val bestStoreUrl: String,
){
    fun toModel() = ProductModel(
        id = id,
        name = name,
        ocrName = ocrName,
        bestPrice = bestPrice,
        purchaseInstalments = purchaseInstalments,
        unitName = unitName,
        bestStore = StoreMetadataModel(
            id = bestStoreId,
            name = bestStoreName,
            url = bestStoreUrl
        )
    )
}