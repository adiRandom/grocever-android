package app.adi_random.dealscraper.services.api

import app.adi_random.dealscraper.data.dto.ApiResponse
import app.adi_random.dealscraper.data.dto.ProductDto
import retrofit2.http.GET

interface ProductApi {
    @GET("/product/list")
    suspend fun getProducts(): ApiResponse<List<ProductDto>>
}