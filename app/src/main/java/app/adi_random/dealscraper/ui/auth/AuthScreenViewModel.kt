package app.adi_random.dealscraper.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.adi_random.dealscraper.data.dto.auth.LoginDto
import app.adi_random.dealscraper.data.repository.AuthRepository
import app.adi_random.dealscraper.data.repository.ResultWrapper
import app.adi_random.dealscraper.services.api.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthScreenViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _isLogin = MutableStateFlow(true)
    val isLogin = _isLogin.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _navigateToMain = MutableSharedFlow<Unit>()
    val navigateToMain = _navigateToMain.asSharedFlow()

    fun setEmail(value: String) {
        _email.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    fun setUsername(value: String) {
        _username.value = value
    }

    fun toggleIsLogin() {
        _isLogin.value = _isLogin.value.not()
    }

    fun login() {
        _error.value = ""

        if (username.value.isEmpty() || password.value.isEmpty()) {
            _error.value = "Email and password cannot be empty"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            authRepository.login(username.value, password.value).collect { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _navigateToMain.emit(Unit)
                        toggleIsLogin()
                    }
                    is ResultWrapper.Error -> {
                        _error.value = result.msg
                    }
                    is ResultWrapper.Loading -> {
                        _isLoading.value = result.isLoading
                    }
                }
            }
        }
    }

    fun register() {
        _error.value = ""

        if (email.value.isEmpty() || password.value.isEmpty() || username.value.isEmpty()) {
            _error.value = "All fields are required"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            authRepository.register(username.value, password.value, email.value).collect { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _navigateToMain.emit(Unit)
                        toggleIsLogin()
                    }
                    is ResultWrapper.Error -> {
                        _error.value = result.msg
                    }
                    is ResultWrapper.Loading -> {
                        _isLoading.value = result.isLoading
                    }
                }
            }
        }
    }
}