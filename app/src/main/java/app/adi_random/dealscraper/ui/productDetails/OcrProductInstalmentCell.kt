package app.adi_random.dealscraper.ui.productDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.adi_random.dealscraper.data.models.UserProductInstalment
import app.adi_random.dealscraper.ui.theme.Colors

@Composable
fun OcrProductInstalmentCell(
    instalment: UserProductInstalment,
    measureUnit: String,
    topRoundCorner: Boolean = false,
    bottomRoundCorner: Boolean = false
) {
    val topCorner = if (topRoundCorner) {
        8.dp
    } else {
        0.dp
    }

    val bottomCorner = if (bottomRoundCorner) {
        8.dp
    } else {
        0.dp
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
            .background(
                color = Colors.AccentBackground,
                shape = RoundedCornerShape(
                    topCorner,
                    topCorner,
                    bottomCorner,
                    bottomCorner
                )
            )
            .padding(8.dp)
    ) {
        Text(
            text = "${instalment.ocrName} X ${instalment.qty} $measureUnit for ${instalment.unitPrice} / $measureUnit",
            color = Colors.TextPrimary
        )
    }
}