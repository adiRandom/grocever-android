package app.adi_random.dealscraper.services.api

import app.adi_random.dealscraper.data.dto.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface OcrProductApi {
    @POST("/ocr")
    @Multipart
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): ApiResponse<Unit>
}