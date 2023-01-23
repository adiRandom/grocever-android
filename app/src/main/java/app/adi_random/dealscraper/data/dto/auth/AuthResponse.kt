package app.adi_random.dealscraper.data.dto.auth

data class AuthResponse(
    val accessToken: String,
    val refresh: String,
    val user: UserDto
)
