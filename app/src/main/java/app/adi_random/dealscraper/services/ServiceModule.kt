package app.adi_random.dealscraper.services

import app.adi_random.dealscraper.services.images.ImageDetectionService
import app.adi_random.dealscraper.services.images.ImageUploadService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val servicesModule = module {
    single {
        ImageDetectionService()
    }
    single {
        ImageUploadService(androidContext(), get(), get(), get())
    }
}