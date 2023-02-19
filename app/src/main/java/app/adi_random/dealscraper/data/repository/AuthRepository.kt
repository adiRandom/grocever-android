package app.adi_random.dealscraper.data.repository

import app.adi_random.dealscraper.data.dao.AuthDao
import app.adi_random.dealscraper.data.dto.auth.AuthResponse
import app.adi_random.dealscraper.data.dto.auth.LoginDto
import app.adi_random.dealscraper.data.dto.auth.RegisterDto
import app.adi_random.dealscraper.data.dto.auth.UserDto
import app.adi_random.dealscraper.services.api.AuthApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val authDao: AuthDao,
    private val preferencesRepository: PreferencesRepository,
    private val authApi: AuthApi
) {
    fun login(username: String, password: String): Flow<ResultWrapper<Unit>> = flow{
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
            }
            is ResultWrapper.Error -> {
                emit(ResultWrapper.Error(result.msg))
            }
            else -> {}
        }

        emit(ResultWrapper.Loading(false))
    }

    private fun onSuccessLogin(authResponse: AuthResponse) {
        val userEntity = authResponse.user.toModel().toEntity()
        authDao.saveUser(userEntity)

        preferencesRepository.saveToken(authResponse.accessToken)
        preferencesRepository.saveRefreshToken(authResponse.refresh)
    }

    fun isLoggedIn(): Boolean {
        return authDao.getUser() != null
    }

    private fun deleteCurrentUser(){
        authDao.deleteAll()
    }


}