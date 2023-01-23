package app.adi_random.dealscraper.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.adi_random.dealscraper.data.dto.auth.LoginDto
import app.adi_random.dealscraper.services.api.AuthApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthScreenViewModel(
    val authApi: AuthApi
) : ViewModel(
) {
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

        if (email.value.isEmpty() || password.value.isEmpty()) {
            _error.value = "Email and password cannot be empty"
            return
        }

        viewModelScope.launch {
            val result =
                authApi.login(LoginDto(username = username.value, password = password.value))

            if (result.error != "") {
                _error.value = result.error
            } else {
                // Save the data
            }
        }
    }

    fun register() {
        _error.value = ""

        if (email.value.isEmpty() || password.value.isEmpty()) {
            _error.value = "Email and password cannot be empty"
            return
        }

        // Call the repo
    }
}