package app.adi_random.dealscraper.data.repository

import app.adi_random.dealscraper.data.dao.ProductDao
import app.adi_random.dealscraper.data.entity.ProductWithPurchaseInstalmentsRelation
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.services.api.ProductApi
import kotlinx.coroutines.flow.flow

class ProductRepository(private val api: ProductApi, private val dao: ProductDao) {
    fun getProductList() = flow {
        emit(ResultWrapper.Loading(true))
        val apiResponse = api.getProducts()
        if (apiResponse.isSuccessful) {
            val products = apiResponse.body.products
            val productModels = products.map { it.toModel() }
            saveProductList(productModels)
            emit(ResultWrapper.Success(productModels))
        } else {
            emit(ResultWrapper.Error(apiResponse.error))
        }

        emit(ResultWrapper.Loading(false))
    }

    private fun saveProductList(products: List<ProductModel>) {
        val productEntities: List<ProductWithPurchaseInstalmentsRelation> =
            products.map { product ->
                ProductWithPurchaseInstalmentsRelation(
                    product = product.toEntity(),
                    purchaseInstalments = product.purchaseInstalments.map { it.toEntity(product.name) }
                )
            }

        dao.saveProducts(productEntities)
    }

    fun getProduct(name: String): ProductModel? {
        val product = dao.getProductByName(name)
        return product?.toModel()
    }
}