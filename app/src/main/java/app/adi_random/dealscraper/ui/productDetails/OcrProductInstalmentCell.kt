package app.adi_random.dealscraper.ui.productDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.adi_random.dealscraper.data.models.UserProductInstalment
import app.adi_random.dealscraper.ui.theme.Colors
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.data.models.ManualAddProductModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun OcrProductInstalmentCell(
    instalment: UserProductInstalment,
    measureUnit: String,
    topRoundCorner: Boolean = false,
    bottomRoundCorner: Boolean = false,
    onEdit: (instalment: UserProductInstalment) -> Unit = {},
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
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
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
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "from ${instalment.store.name} on ${SimpleDateFormat("dd/MM/yy").format(Date(instalment.date * 1000))}",
                color = Colors.TextDisabled,
                fontSize = 12.sp
            )
            Image(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "edit",
                modifier = Modifier
                    .clickable { onEdit(instalment) }
                    .padding(0.dp, 8.dp, 0.dp, 0.dp)
                    .size(16.dp)
            )
        }
    }
}