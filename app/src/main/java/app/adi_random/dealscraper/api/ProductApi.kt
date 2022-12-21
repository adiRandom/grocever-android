package app.adi_random.dealscraper.api

import app.adi_random.dealscraper.data.dto.ProductDto
import retrofit2.http.GET

interface ProductApi {
    @GET("/product/list")
    suspend fun getProducts(): List<ProductDto>
}