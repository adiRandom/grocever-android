package app.adi_random.dealscraper.services.api

import app.adi_random.dealscraper.data.dto.store.ApiResponse
import app.adi_random.dealscraper.data.dto.auth.AuthResponse
import app.adi_random.dealscraper.data.dto.auth.LoginDto
import app.adi_random.dealscraper.data.dto.auth.RegisterDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/login")
    suspend fun login(@Body body: LoginDto): ApiResponse<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body body: RegisterDto): ApiResponse<AuthResponse>
}