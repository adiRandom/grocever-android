package app.adi_random.dealscraper.services.api

import org.koin.dsl.module
import app.adi_random.dealscraper.BuildConfig
import app.adi_random.dealscraper.data.repository.PreferencesRepository
import app.adi_random.dealscraper.services.auth.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

val apiModule = module {

    fun getOkHttpClient(preferencesRepository: PreferencesRepository): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(AuthInterceptor(preferencesRepository)).build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(getOkHttpClient(get()))
            .build()
    }
    single {
        get<Retrofit>().create(ProductApi::class.java)
    }
    single {
        get<Retrofit>().create(AuthApi::class.java)
    }
}