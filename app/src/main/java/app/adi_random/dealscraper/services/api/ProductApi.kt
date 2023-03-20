package app.adi_random.dealscraper.services.api

import app.adi_random.dealscraper.data.dto.product.CreateProductDto
import app.adi_random.dealscraper.data.dto.product.ReportMissLinkDto
import app.adi_random.dealscraper.data.dto.product.UserProductInstalmentDto
import app.adi_random.dealscraper.data.dto.store.ApiResponse
import app.adi_random.dealscraper.data.dto.product.UserProductListDto
import retrofit2.http.*

interface ProductApi {
    @GET("/product/list")
    suspend fun getProducts():
            ApiResponse<UserProductListDto>

    @POST("/product")
    suspend fun addProduct(@Body product: CreateProductDto):
            ApiResponse<UserProductInstalmentDto>

    @POST("/product/report")
    suspend fun reportMissLink(@Body dto: ReportMissLinkDto):
            ApiResponse<Unit>

    @GET("/product/report/list")
    suspend fun getReportedProducts():
            ApiResponse<List<ReportMissLinkDto>>

    @PUT("/product/{id}")
    suspend fun updateProduct(@Path("id") id:Int, @Body product: CreateProductDto):
            ApiResponse<UserProductInstalmentDto>
}