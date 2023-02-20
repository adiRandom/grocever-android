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

    fun saveLastCheckedImageId(id: Long) {
        sharedPreferences.edit().putLong(LAST_CHECKED_IMAGE_URI, id).apply()
    }

    fun getLastCheckedImageId(): Long? {
        val id = sharedPreferences.getLong(LAST_CHECKED_IMAGE_URI, -1)
        return if (id == -1L) null else id
    }

    companion object {
        const val TOKEN = "token"
        const val REFRESH_TOKEN = "refreshToken"
        const val LAST_CHECKED_IMAGE_URI = "lastCheckedImageUri"
    }
}