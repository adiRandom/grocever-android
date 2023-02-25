package app.adi_random.dealscraper.data

import app.adi_random.dealscraper.data.repository.AuthRepository
import app.adi_random.dealscraper.services.api.AuthApi
import app.adi_random.dealscraper.services.api.getBasicOkHttpBuilder
import app.adi_random.dealscraper.services.api.getBasicRetrofitClientBuilder
import org.koin.dsl.module
import retrofit2.Retrofit

val authModule = module {
    fun provideAuthDao(db: AppDatabase) = db.authDao()

    single {
        provideAuthDao(get())
    }
    single<AuthApi> {
        getBasicRetrofitClientBuilder(getBasicOkHttpBuilder(get()).build())
            .build()
            .create(AuthApi::class.java)
    }
    single {
        AuthRepository(get(), get(), get())
    }
}