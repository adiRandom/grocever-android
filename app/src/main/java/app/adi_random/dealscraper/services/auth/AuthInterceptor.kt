package app.adi_random.dealscraper.services.auth

import android.content.SharedPreferences
import app.adi_random.dealscraper.data.repository.PreferencesRepository
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit

class AuthInterceptor(private val preferencesRepository: PreferencesRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = preferencesRepository.getToken() ?: return chain.proceed(chain.request())

        val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token").build()
                return chain.proceed(request)
    }
}