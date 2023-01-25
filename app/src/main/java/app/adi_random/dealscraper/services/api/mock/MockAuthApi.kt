package app.adi_random.dealscraper.services.api.mock

import app.adi_random.dealscraper.data.dto.ApiResponse
import app.adi_random.dealscraper.data.dto.auth.AuthResponse
import app.adi_random.dealscraper.data.dto.auth.LoginDto
import app.adi_random.dealscraper.data.dto.auth.RegisterDto
import app.adi_random.dealscraper.data.dto.auth.UserDto
import app.adi_random.dealscraper.services.api.AuthApi

class MockAuthApi : AuthApi {
    override suspend fun login(body: LoginDto): ApiResponse<AuthResponse> {
        // Pick a random number between 0 and 1. If the number is over 0.3 then return a success response
        // otherwise return an error response
        val random = Math.random()
        return if (random > 0.3) {
            ApiResponse(200, AuthResponse("axxa", "axasda", UserDto(1, body.username, "email")), "")
        } else {
            ApiResponse(500, AuthResponse("", "", UserDto(0, "", "")), "ISE")

        }

    }

    override suspend fun register(body: RegisterDto): ApiResponse<AuthResponse> {
        val random = Math.random()
        return if (random > 0.3) {
            ApiResponse(
                200,
                AuthResponse("axxa", "axasda", UserDto(1, body.username, body.email)),
                ""
            )
        } else {
            ApiResponse(500, AuthResponse("", "", UserDto(0, "", "")), "ISE")

        }
    }
}