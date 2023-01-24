package app.adi_random.dealscraper.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import app.adi_random.dealscraper.data.repository.PreferencesRepository
import org.koin.dsl.module

const val SHARED_PREFS_NAME = "GROCEVER_SHARED_PREFS"

val sharedPrefsModule = module{
    fun provideSharedPreferences(app: Application): SharedPreferences? {
        return app.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }
    single{
        provideSharedPreferences(get())
    }
    single{
        PreferencesRepository(get())
    }
}