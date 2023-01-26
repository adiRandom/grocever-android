package app.adi_random.dealscraper.data.repository

import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.services.api.ProductApi
import kotlinx.coroutines.flow.flow

class ProductRepository(private val api: ProductApi) {
    fun getProductList() = flow {
        emit(ResultWrapper.Loading(true))
        val apiResponse = api.getProducts()
        if (apiResponse.isSuccessful) {
            val products = apiResponse.body.products
            val productModels = products.map { it.toModel() }
            emit(ResultWrapper.Success(productModels))
        } else {
            emit(ResultWrapper.Error(apiResponse.error))
        }

        emit(ResultWrapper.Loading(false))
    }
}