package app.adi_random.dealscraper.services.api

import app.adi_random.dealscraper.data.dto.product.StoreMetadataDto
import app.adi_random.dealscraper.data.dto.store.ApiResponse
import retrofit2.http.GET

interface StoreApi {
    @GET("/store/list")
    suspend fun getStores():
            ApiResponse<List<StoreMetadataDto>>
}