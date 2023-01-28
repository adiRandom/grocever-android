package app.adi_random.dealscraper.ui.productDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.adi_random.dealscraper.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import app.adi_random.dealscraper.ui.theme.Colors


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ProductDetails(viewModel: ProductDetailsViewModel) {
    Column(Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp)) {
        Text(
            text = stringResource(id = R.string.you_bought),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
        )
        // TODO: Add report button

        val productInstalmentsByOcrName by viewModel.productInstalmentsByOcrName.collectAsStateWithLifecycle()
        val product by viewModel.product.collectAsStateWithLifecycle()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp, 8.dp)
        ) {
            items(
                items = productInstalmentsByOcrName ?: emptyList(),
                key = { it.first }
            ) { (ocrName, instalments) ->
                Text(
                    text = ocrName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 8.dp)
                )
                instalments.forEachIndexed { index, instalment ->
                    OcrProductInstalmentCell(
                        instalment = instalment,
                        measureUnit = product?.unitName ?: "",
                        topRoundCorner = index == 0,
                        bottomRoundCorner = index == instalments.size - 1
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Colors.Primary,
                    shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
                )
                .padding(8.dp)
        ) {
            val savings by viewModel.savings.collectAsStateWithLifecycle()

            Text(
                text = stringResource(id = R.string.product_savings),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Colors.TextOnPrimary
            )
            Text(
                text = "$savings RON",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.TextOnPrimary,
                modifier = Modifier.padding(0.dp, 8.dp)
            )

            if (product != null) {
                BestProductCard(product = product!!)
            }
        }
    }
}