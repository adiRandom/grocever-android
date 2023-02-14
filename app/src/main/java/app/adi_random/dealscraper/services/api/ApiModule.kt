package app.adi_random.dealscraper.services.api

import org.koin.dsl.module
import app.adi_random.dealscraper.BuildConfig
import app.adi_random.dealscraper.data.repository.PreferencesRepository
import app.adi_random.dealscraper.services.api.mock.MockAuthApi
import app.adi_random.dealscraper.services.api.mock.MockProductApi
import app.adi_random.dealscraper.services.auth.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module {
    fun getOkHttpClient(preferencesRepository: PreferencesRepository): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(AuthInterceptor(preferencesRepository)).build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient(get()))
            .build()
    }
    single<ProductApi> {
        get<Retrofit>().create(ProductApi::class.java)
//        MockProductApi()
    }
    single<AuthApi> {
        get<Retrofit>().create(AuthApi::class.java)
//        MockAuthApi()
    }
}