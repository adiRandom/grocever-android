package app.adi_random.dealscraper.data

import android.app.Application
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import org.koin.dsl.module
import app.adi_random.dealscraper.BuildConfig
import app.adi_random.dealscraper.data.repository.AuthRepository
import app.adi_random.dealscraper.data.repository.ProductRepository
import retrofit2.Retrofit

const val DB_NAME = "GROCEVER_DB"

val dataModule = module {
    fun provideDatabase(
        application: Application,
    ): AppDatabase {
        return databaseBuilder(application, AppDatabase::class.java, DB_NAME)
            .build()
    }

    fun provideAuthDao(db: AppDatabase) = db.authDao()
    fun provideProductDao(db: AppDatabase) = db.productDao()


    single {
        provideDatabase(get())
    }
    single {
        provideAuthDao(get())
    }
    single {
        provideProductDao(get())
    }

    single {
        AuthRepository(get(), get(), get())
    }

    single{
        ProductRepository(get(), get())
    }
}