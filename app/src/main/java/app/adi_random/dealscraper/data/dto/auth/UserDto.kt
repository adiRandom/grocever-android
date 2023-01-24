package app.adi_random.dealscraper.data.dto.auth

import app.adi_random.dealscraper.data.models.UserModel

data class UserDto(val id: Int, val username: String, val email: String) {
    fun toModel(): UserModel {
        return UserModel(id, username, email)
    }
}
