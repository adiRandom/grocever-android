package app.adi_random.dealscraper.data.repository

import android.content.SharedPreferences

class PreferencesRepository(private val sharedPreferences: SharedPreferences) {
    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN, token).apply()
    }

    fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit().putString(REFRESH_TOKEN, refreshToken).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN, null)
    }

    companion object {
        const val TOKEN = "token"
        const val REFRESH_TOKEN = "refreshToken"
    }
}