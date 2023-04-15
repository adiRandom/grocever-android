package app.adi_random.dealscraper.ui.weeklyOverview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.ui.theme.Colors
import java.util.Date

@Composable
fun WeeklyOverviewHeader(actualSpending: Float, savings: Float, startDate: Long, endDate: Long) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Colors.Primary,
                shape = RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp)
            )
            .padding(24.dp, 16.dp, 24.dp, 16.dp)
    ) {
        Text(
            text = "Between",
            color = Colors.TextOnPrimary,
            fontSize = 16.sp,
        )
        Text(
            text = "${
                Date(startDate).toString().substring(0, 10)
            } and ${Date(endDate).toString().substring(0, 10)}",
            color = Colors.TextOnPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "you spent",
            color = Colors.TextOnPrimary,
            fontSize = 16.sp,
        )
        Text(
            "${String.format("%.2f", actualSpending)} RON",
            color = Colors.TextOnPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(id = R.string.savings1),
                color = Colors.TextOnPrimary,
                fontSize = 16.sp
            )
            Text(
                text = stringResource(id = R.string.savings2),
                color = Colors.Secondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(6.dp, 0.dp)
            )
            Text(
                text = stringResource(id = R.string.savings3),
                color = Colors.TextOnPrimary,
                fontSize = 16.sp
            )
        }
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.offset(x = (-18).dp)) {
            Text(
                text = stringResource(id = R.string.savings4),
                color = Colors.TextOnPrimary,
                fontSize = 12.sp,
                modifier = Modifier
                    .offset(y = (-4).dp)
                    .padding(4.dp, 0.dp)
            )
            Text(
                "${String.format("%.2f", savings)} RON",
                color = Colors.TextOnPrimary,
                fontSize = 40.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}