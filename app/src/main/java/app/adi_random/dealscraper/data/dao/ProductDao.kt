package app.adi_random.dealscraper.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import app.adi_random.dealscraper.data.entity.ProductEntity
import app.adi_random.dealscraper.data.entity.ProductWithPurchaseInstalmentsRelation
import app.adi_random.dealscraper.data.entity.PurchaseInstalmentEntity

@Dao
abstract class ProductDao {
    @Transaction
    @Query("SELECT * FROM products")
    abstract fun getAllProducts(): List<ProductWithPurchaseInstalmentsRelation>

    fun saveProducts(products: List<ProductWithPurchaseInstalmentsRelation>) {
        products.forEach { product ->
            insertProduct(product.product)
            product.purchaseInstalments.forEach { purchaseInstalment ->
                insertPurchaseInstalment(purchaseInstalment)
            }
        }
    }

    @Insert
    abstract fun insertProduct(product: ProductEntity)

    @Insert
    abstract fun insertPurchaseInstalment(purchaseInstalment: PurchaseInstalmentEntity)

    @Query("DELETE FROM products")
    abstract fun deleteAllProducts()

    @Query("DELETE FROM purchase_instalments")
    abstract fun deleteAllPurchaseInstalments()

    fun deleteAllProductsAndPurchaseInstalments() {
        deleteAllProducts()
        deleteAllPurchaseInstalments()
    }

    @Query("SELECT * FROM products WHERE name = :name")
    abstract fun getProductByName(name: String): ProductWithPurchaseInstalmentsRelation?

}