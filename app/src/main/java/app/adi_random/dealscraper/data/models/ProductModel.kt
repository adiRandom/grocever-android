package app.adi_random.dealscraper.data.models

import android.net.Uri
import app.adi_random.dealscraper.data.dto.product.UserProductInstalmentDto
import app.adi_random.dealscraper.data.entity.ProductEntity

data class ProductModel(
    val id: Int,
    val name: String,
    val bestPrice: Float,
    val purchaseInstalments: List<UserProductInstalment>,
    val bestStore: StoreMetadataModel,
    val url: String,
    val imageUrl: String
){
    fun toEntity() = ProductEntity(
        id = id,
        name = name,
        bestPrice = bestPrice,
        bestStore = bestStore,
        url = url,
        imageUrl = imageUrl
    )
}