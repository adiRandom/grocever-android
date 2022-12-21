package app.adi_random.dealscraper

import android.app.Application
import app.adi_random.dealscraper.api.apiModule
import app.adi_random.dealscraper.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class GroceverApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GroceverApplication)
            modules(listOf(apiModule, uiModule))
        }
    }
}
