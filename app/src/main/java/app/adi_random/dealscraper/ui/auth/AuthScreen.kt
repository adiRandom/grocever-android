package app.adi_random.dealscraper.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AuthScreen(viewModel: AuthScreenViewModel = koinViewModel()) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        val username by viewModel.username.collectAsStateWithLifecycle()
        val email by viewModel.email.collectAsStateWithLifecycle()
        val password by viewModel.password.collectAsStateWithLifecycle()
        val isLogin by viewModel.isLogin.collectAsStateWithLifecycle()

        // TODO: Add logo

        if (isLogin) {
            LoginForm(
                username = username,
                password = password,
                setUsername = viewModel::setUsername,
                setPassword = viewModel::setPassword
            )
        } else {
            RegisterForm(
                username = username,
                email = email,
                password = password,
                setUsername = viewModel::setUsername,
                setEmail = viewModel::setEmail,
                setPassword = viewModel::setPassword
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        AuthButtonBar(
            isLogin = isLogin,
            toggle = viewModel::toggleIsLogin,
            onLogin = viewModel::login,
            onRegister = viewModel::register
        )
    }
}
