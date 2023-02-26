package app.adi_random.dealscraper.ui.misc

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.adi_random.dealscraper.R

enum class InfoStatus {
    SUCCESS, ERROR
}

@Composable
fun InfoBottomSheet(msg: String, status: InfoStatus) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 32.dp)
    ) {

        Image(
            painter =
            painterResource(
                id = when (status) {
                    InfoStatus.SUCCESS -> {
                        R.drawable.ic_check
                    }
                    InfoStatus.ERROR -> {
                        R.drawable.ic_error
                    }
                }
            ), contentDescription = "status_icon",
            modifier = Modifier.size(48.dp)
        )
        Text(text = msg, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}