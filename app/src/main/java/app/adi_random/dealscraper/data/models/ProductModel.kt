package app.adi_random.dealscraper.data.models

import app.adi_random.dealscraper.data.dto.product.UserProductInstalmentDto

data class ProductModel(
    val id: Int,
    val name: String,
    val bestPrice: Float,
    val purchaseInstalments: List<UserProductInstalment>,
    val unitName: String,
    val bestStore: StoreMetadataModel
)