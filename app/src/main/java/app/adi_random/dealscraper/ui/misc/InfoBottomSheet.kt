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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.data.models.bottomSheet.InfoMessageBottomSheetModel

enum class InfoStatus {
    SUCCESS, ERROR
}

@Composable
fun InfoBottomSheet(model: InfoMessageBottomSheetModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 32.dp)
    ) {
        Image(
            painter =
            painterResource(
                id = when (model.status) {
                    InfoStatus.SUCCESS -> {
                        R.drawable.ic_check
                    }
                    InfoStatus.ERROR -> {
                        R.drawable.ic_error
                    }
                }
            ), contentDescription = "status_icon",
            modifier = Modifier.size(48.dp))
        Text(text = model.message, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
    }
}