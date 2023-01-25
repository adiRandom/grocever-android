package app.adi_random.dealscraper.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import app.adi_random.dealscraper.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthDao {
    @Query("SELECT * FROM userEntity")
    fun getUser(): UserEntity?

    @Query("SELECT * FROM userEntity")
    fun getUserFlow(): Flow<UserEntity?>

    @Insert
    fun saveUser(user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)

    @Query("DELETE FROM userEntity")
    fun deleteAll()
}