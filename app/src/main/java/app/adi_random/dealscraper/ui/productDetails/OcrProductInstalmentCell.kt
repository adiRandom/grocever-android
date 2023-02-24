package app.adi_random.dealscraper.ui.productDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
            .background(
                color = Colors.PrimaryLight,
                shape = RoundedCornerShape(
                    topCorner,
                    topCorner,
                    bottomCorner,
                    bottomCorner
                )
            )
            .padding(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "${instalment.qty} $measureUnit",
                color = Colors.TextOnPrimary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 8.dp, 0.dp)
            )

            Text(
                text = "${instalment.unitPrice} RON / $measureUnit",
                color = Colors.TextOnPrimary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(8.dp, 0.dp, 0.dp, 0.dp)
            )
        }
        Text(
            text = "from ${instalment.store.name}",
            color = Colors.TextDisabled,
            fontSize = 12.sp
        )
    }
}