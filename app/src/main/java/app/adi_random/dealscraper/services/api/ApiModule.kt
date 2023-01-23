package app.adi_random.dealscraper.services.api

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
    single{
        get<Retrofit>().create(AuthApi::class.java)
    }
}