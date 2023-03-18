package app.adi_random.dealscraper.data

import android.app.Application
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import org.koin.dsl.module
import app.adi_random.dealscraper.BuildConfig
import app.adi_random.dealscraper.data.repository.*
import org.koin.android.ext.koin.androidContext
import retrofit2.Retrofit

const val DB_NAME = "GROCEVER_DB"

val dataModule = module {
    fun provideProductDao(db: AppDatabase) = db.productDao()
    fun provideRetryUploadDao(db: AppDatabase) = db.retryUploadDao()


    single {
        provideRetryUploadDao(get())
    }
    single {
        provideProductDao(get())
    }

    single{
        ProductRepository(get(), get())
    }
    single{
        OcrProductRepository(get())
    }
    single{
        GalleryRepository(androidContext().contentResolver, get(), get())
    }
    single{
       StoreRepository(get())
    }
}