package app.adi_random.dealscraper.data.models

import app.adi_random.dealscraper.data.entity.UserEntity

data class UserModel(
    val id: Int,
    val username: String,
    val email: String
){
    fun toEntity(): UserEntity{
        return UserEntity(id, username, email)
    }
}
