package app.adi_random.dealscraper.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import app.adi_random.dealscraper.data.entity.UserEntity

@Dao
interface AuthDao {
    @Query("SELECT * FROM userEntity")
    fun getUser(): UserEntity

    @Insert
    fun saveUser(user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)
}