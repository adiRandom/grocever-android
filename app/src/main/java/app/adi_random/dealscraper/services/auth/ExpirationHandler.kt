package app.adi_random.dealscraper.services.auth

import app.adi_random.dealscraper.data.repository.AuthRepository
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class ExpirationHandler(private val authRepository: AuthRepository) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (authRepository.isLoggedIn().not()) {
            return null
        }

        val newToken = authRepository.refreshAccessToken() ?: return null
        return response.request.newBuilder().removeHeader("Authorization")
            .addHeader("Authorization", "Bearer $newToken").build()
    }
}