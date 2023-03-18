package app.adi_random.dealscraper.services.api

import app.adi_random.dealscraper.data.dto.notification.SetFcmTokenDto
import app.adi_random.dealscraper.data.dto.store.ApiResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface NotificationApi {
    @PUT("/notification/token")
    suspend fun updateToken(@Body body: SetFcmTokenDto):Response<ApiResponse<Unit>>
}