package app.adi_random.dealscraper.data

import android.app.Application
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import org.koin.dsl.module
import app.adi_random.dealscraper.BuildConfig
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


    single {
        provideDatabase(get())
    }
    single {
        provideAuthDao(get())
    }

}