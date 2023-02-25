package app.adi_random.dealscraper.services.api

import app.adi_random.dealscraper.data.dto.product.CreateProductDot
import app.adi_random.dealscraper.data.dto.product.UserProductInstalmentDto
import app.adi_random.dealscraper.data.dto.store.ApiResponse
import app.adi_random.dealscraper.data.dto.product.UserProductListDto
import app.adi_random.dealscraper.data.entity.PurchaseInstalmentEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductApi {
    @GET("/product/list")
    suspend fun getProducts():
            ApiResponse<UserProductListDto>

    @POST("/product")
    suspend fun addProduct(@Body product: CreateProductDot):
            ApiResponse<UserProductInstalmentDto>
}