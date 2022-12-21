package app.adi_random.dealscraper.api

import org.koin.dsl.module
import app.adi_random.dealscraper.BuildConfig
import retrofit2.Retrofit

val apiModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .build()
    }
    single {
        get<Retrofit>().create(ProductApi::class.java)
    }
}