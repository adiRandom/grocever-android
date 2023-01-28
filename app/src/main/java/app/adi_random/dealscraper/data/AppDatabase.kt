package app.adi_random.dealscraper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import app.adi_random.dealscraper.data.dao.AuthDao
import app.adi_random.dealscraper.data.dao.ProductDao
import app.adi_random.dealscraper.data.entity.UserEntity


@Database(
    version = 1,
    entities = [UserEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
    abstract fun productDao(): ProductDao
}