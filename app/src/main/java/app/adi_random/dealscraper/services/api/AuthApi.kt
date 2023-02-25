package app.adi_random.dealscraper.services.api

import app.adi_random.dealscraper.data.dto.auth.*
import app.adi_random.dealscraper.data.dto.store.ApiResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/login")
    suspend fun login(@Body body: LoginDto): ApiResponse<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body body: RegisterDto): ApiResponse<AuthResponse>

    @POST("/auth/refresh")
    fun refresh(@Body body: RefreshDto): Call<ApiResponse<RefreshResponseDto>>
}