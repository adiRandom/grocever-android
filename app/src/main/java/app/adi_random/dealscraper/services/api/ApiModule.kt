package app.adi_random.dealscraper.services.api

import org.koin.dsl.module
import app.adi_random.dealscraper.BuildConfig
import app.adi_random.dealscraper.data.repository.AuthRepository
import app.adi_random.dealscraper.data.repository.PreferencesRepository
import app.adi_random.dealscraper.services.auth.AuthInterceptor
import app.adi_random.dealscraper.services.auth.ExpirationHandler
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getBasicOkHttpBuilder(preferencesRepository: PreferencesRepository): OkHttpClient.Builder {
    return OkHttpClient.Builder().addInterceptor(AuthInterceptor(preferencesRepository))
}

fun getBasicRetrofitClientBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
}

val apiModule = module {

    fun provideOkHttpClient(
        preferencesRepository: PreferencesRepository,
        authRepository: AuthRepository
    ): OkHttpClient {
        return getBasicOkHttpBuilder(preferencesRepository).authenticator(
            ExpirationHandler(
                authRepository
            )
        ).build()
    }

    single {
        getBasicRetrofitClientBuilder(provideOkHttpClient(get(), get()))
            .build()
    }
    single<ProductApi> {
        get<Retrofit>().create(ProductApi::class.java)
    }

    single<OcrProductApi> {
        get<Retrofit>().create(OcrProductApi::class.java)
    }
    single<StoreApi> {
        get<Retrofit>().create(StoreApi::class.java)
    }
    single<NotificationApi> {
        get<Retrofit>().create(NotificationApi::class.java)
    }
}