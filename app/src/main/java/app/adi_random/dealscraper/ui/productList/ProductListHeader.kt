package app.adi_random.dealscraper.ui.productList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.ui.theme.Colors

@Composable
fun ProductListHeader(actualSpending: Float, savings: Float) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Colors.Primary,
                shape = RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp)
            )
            .padding(16.dp, 24.dp)
    ) {
        Text(
            stringResource(id = R.string.actual_spending),
            color = Colors.TextOnPrimary,
            fontSize = 16.sp
        )
        Text(
            "$actualSpending RON",
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
                "$savings RON",
                color = Colors.TextOnPrimary,
                fontSize = 40.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}