package app.adi_random.dealscraper.data.repository

import app.adi_random.dealscraper.data.dao.ProductDao
import app.adi_random.dealscraper.data.dto.product.ReportMissLinkDto
import app.adi_random.dealscraper.data.entity.ProductWithPurchaseInstalmentsRelation
import app.adi_random.dealscraper.data.models.*
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

    fun getProductListBetweenDates(startDate: Long, endDate: Long): List<ProductModel> {
        val productList = dao.getAllProducts()
        for (product in productList) {
            val purchaseInstalments = product.purchaseInstalments
            // Convert s to ms
            val filteredPurchaseInstalments = purchaseInstalments.filter { it.date * 1000 in startDate..endDate }
            product.purchaseInstalments = filteredPurchaseInstalments
        }

        return productList.filter { it.purchaseInstalments.isNotEmpty() }.map { it.toModel() }
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

    suspend fun reportMissLink(productId: Int, ocrProductName: String) {
        val dto = ReportMissLinkDto(productId, ocrProductName)
        api.reportMissLink(dto)
    }

    suspend fun getReportedProducts(): List<ReportMissLinkModel> {
        val apiResponse = api.getReportedProducts()
        return apiResponse.body.map { it.toModel() }
    }

    suspend fun editProduct(product: EditPurchaseInstalmentModel): UserProductInstalment {
        val dto = product.toDto()
        val result = api.updateProduct(product.id, dto)
        if(result.isSuccessful){
           return result.body.toModel()
        }

        throw Exception("Failed to update product")
    }

}