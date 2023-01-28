package app.adi_random.dealscraper.data.entity

import androidx.room.Embedded
import androidx.room.Relation
import app.adi_random.dealscraper.data.models.ProductModel

data class ProductWithPurchaseInstalmentsRelation(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "name",
        entityColumn = "productName"
    )
    val purchaseInstalments: List<PurchaseInstalmentEntity>
){
    fun toModel() = ProductModel(
        name = product.name,
        bestPrice = product.bestPrice,
        unitName = product.unitName,
        bestStore = product.bestStore,
        purchaseInstalments = purchaseInstalments.map { it.toModel() }
    )
}