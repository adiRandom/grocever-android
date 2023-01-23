package app.adi_random.dealscraper.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import app.adi_random.dealscraper.R

@Composable
fun AuthButtonBar(
    isLogin: Boolean,
    toggle: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick =
            if (isLogin) {
                onLogin
            } else {
                onRegister
            }
        ) {
            Text(
                text = stringResource(
                    id = if (isLogin) {
                        R.string.login_btn
                    } else {
                        R.string.register_btn
                    }
                )
            )
        }
        TextButton(onClick = toggle) {
            Text(
                text = stringResource(
                    id = if (isLogin) {
                        R.string.register_btn
                    } else {
                        R.string.login_btn
                    }
                )
            )
        }
    }
}

