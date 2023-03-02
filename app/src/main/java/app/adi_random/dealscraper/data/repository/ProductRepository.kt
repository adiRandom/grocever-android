package app.adi_random.dealscraper.data.repository

import app.adi_random.dealscraper.data.dao.ProductDao
import app.adi_random.dealscraper.data.entity.ProductWithPurchaseInstalmentsRelation
import app.adi_random.dealscraper.data.models.ManualAddProductModel
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.data.models.UserProductInstalment
import app.adi_random.dealscraper.services.api.ProductApi
import kotlinx.coroutines.flow.flow

class ProductRepository(private val api: ProductApi, private val dao: ProductDao) {
    fun getProductList() = flow {
        emit(ResultWrapper.Loading(true))
        val apiResponse = api.getProducts()
        if (apiResponse.isSuccessful) {
            val products = apiResponse.body.products
            val productModels = products.map { it.toModel() }
            emit(ResultWrapper.Success(productModels))
            saveProductList(productModels)
        } else {
            emit(ResultWrapper.Error(apiResponse.err))
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

        dao.deleteAllProductsAndPurchaseInstalments()
        dao.saveProducts(productEntities)
    }

    fun getProduct(id: Int): ProductModel? {
        val product = dao.getProductById(id)
        return product?.toModel()
    }

    fun createProduct(product: ManualAddProductModel) = flow {
        emit(ResultWrapper.Loading(true))
        val dto = product.toDto()
        val apiResponse = api.addProduct(dto)
        when (val result = apiResponse.toResultWrapper()) {
            is ResultWrapper.Success -> {
                val purchaseInstalmentModel = result.data.toModel()
                emit(ResultWrapper.Success(purchaseInstalmentModel))
            }
            is ResultWrapper.Error -> emit(result)
            else -> {
                //do nothing
            }
        }
        emit(ResultWrapper.Loading(false))
    }
}