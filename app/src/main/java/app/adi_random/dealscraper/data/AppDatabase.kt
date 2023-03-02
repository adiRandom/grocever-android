package app.adi_random.dealscraper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import app.adi_random.dealscraper.data.dao.AuthDao
import app.adi_random.dealscraper.data.dao.ProductDao
import app.adi_random.dealscraper.data.dao.RetryUploadDao
import app.adi_random.dealscraper.data.entity.ProductEntity
import app.adi_random.dealscraper.data.entity.PurchaseInstalmentEntity
import app.adi_random.dealscraper.data.entity.RetryUploadEntity
import app.adi_random.dealscraper.data.entity.UserEntity


@Database(
    version = 7,
    entities = [UserEntity::class, ProductEntity::class, PurchaseInstalmentEntity::class, RetryUploadEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
    abstract fun productDao(): ProductDao
    abstract fun retryUploadDao(): RetryUploadDao
}