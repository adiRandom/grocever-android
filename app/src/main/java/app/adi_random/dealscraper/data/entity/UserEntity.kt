package app.adi_random.dealscraper.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.adi_random.dealscraper.data.models.UserModel

@Entity
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val username: String,
    val email: String
){
    fun toModel(): UserModel{
        return UserModel(id, username, email)
    }
}