package app.adi_random.dealscraper.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import app.adi_random.dealscraper.data.repository.PreferencesRepository
import org.koin.dsl.module

const val SHARED_PREFS_NAME = "GROCEVER_SHARED_PREFS"

val dataStoresModule = module{
    fun provideDatabase(
        application: Application,
    ): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, DB_NAME)
            .build()
    }

    fun provideSharedPreferences(app: Application): SharedPreferences? {
        return app.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }


    single{
        provideSharedPreferences(get())
    }
    single{
        PreferencesRepository(get())
    }
    single {
        provideDatabase(get())
    }
}