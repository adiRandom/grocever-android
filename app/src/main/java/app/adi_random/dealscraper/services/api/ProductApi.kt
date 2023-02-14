package app.adi_random.dealscraper.services.api

import app.adi_random.dealscraper.data.dto.ApiResponse
import app.adi_random.dealscraper.data.dto.product.UserProductListDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("/product/list")
    suspend fun getProducts():
            ApiResponse<UserProductListDto>
}