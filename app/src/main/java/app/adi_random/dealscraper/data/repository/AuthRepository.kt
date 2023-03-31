package app.adi_random.dealscraper.data.repository

import app.adi_random.dealscraper.data.dao.AuthDao
import app.adi_random.dealscraper.data.dto.auth.*
import app.adi_random.dealscraper.services.api.AuthApi
import app.adi_random.dealscraper.services.api.NotificationApi
import app.adi_random.dealscraper.ui.navigation.Routes
import kotlinx.coroutines.flow.*
import retrofit2.Retrofit

class AuthRepository(
    private val authDao: AuthDao,
    private val preferencesRepository: PreferencesRepository,
    private val authApi: AuthApi,
) {
    private val _isLoggedIn: MutableStateFlow<Boolean> =
        MutableStateFlow(preferencesRepository.getToken() != null)

    val isLoggedIn: Flow<Boolean> = _isLoggedIn.asStateFlow()
    fun login(username: String, password: String): Flow<ResultWrapper<Unit>> = flow {
        emit(ResultWrapper.Loading(true))
        val response = authApi.login(LoginDto(username, password))
        when (val result = response.toResultWrapper()) {
            is ResultWrapper.Success -> {
                onSuccessLogin(result.data)
                emit(ResultWrapper.Success(Unit))
            }
            is ResultWrapper.Error -> {
                emit(ResultWrapper.Error(result.msg))
            }
            else -> {}
        }

        emit(ResultWrapper.Loading(false))
    }

    fun register(username: String, password: String, email: String): Flow<ResultWrapper<Unit>> = flow{
        emit(ResultWrapper.Loading(true))
        val response = authApi.register(RegisterDto(username, email, password))
        when (val result = response.toResultWrapper()) {
            is ResultWrapper.Success -> {
                onSuccessLogin(result.data)
                emit(ResultWrapper.Success(Unit))
            }
            is ResultWrapper.Error -> {
                emit(ResultWrapper.Error(result.msg))
            }
            else -> {}
        }

        emit(ResultWrapper.Loading(false))
    }

    /**
     * Get new access token
     */
    fun refreshAccessToken(): String? {
        val refreshToken = preferencesRepository.getRefreshToken() ?: return null
        val accessToken = preferencesRepository.getToken() ?: return null
        val response = authApi.refresh(RefreshDto(refreshToken, accessToken)).execute()
        return when(val result = response.body()?.toResultWrapper()) {
            is ResultWrapper.Success -> {
                preferencesRepository.saveToken(result.data.accessToken)
                result.data.accessToken
            }
            is ResultWrapper.Error -> {
                logout()
                null
            }
            else -> {
                null
            }
        }
    }

    fun logout() {
        preferencesRepository.saveToken(null)
        preferencesRepository.saveRefreshToken(null)
        _isLoggedIn.value = false
        authDao.deleteAll()
    }

    private fun onSuccessLogin(authResponse: AuthResponse) {
        val userEntity = authResponse.user.toModel().toEntity()
        authDao.saveUser(userEntity)

        preferencesRepository.saveToken(authResponse.accessToken)
        preferencesRepository.saveRefreshToken(authResponse.refresh)
        _isLoggedIn.value = true
    }

    fun isLoggedIn(): Boolean {
        return _isLoggedIn.value
    }

}