package app.adi_random.dealscraper.ui.productList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.ui.theme.Colors

@Composable
fun ProductEntry(product: ProductModel, openProductDetails: (String) -> Unit) {
    val worstPrice = product.purchaseInstalments.fold(0f) { acc, instalment ->
        val price = instalment.qty * instalment.unitPrice
        if (price > acc) {
            price
        } else {
            acc
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Colors.Background)
            .clickable {
                openProductDetails(product.name)
            }
    ) {
        Row {
            Text(text = product.name, color = Colors.TextPrimary)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 8.dp, 0.dp, 0.dp)
        ) {
            Row {
                Text(
                    text = worstPrice.toString(),
                    color = Colors.TextSecondary,
                    textDecoration = TextDecoration.LineThrough
                )
                Text(
                    text = product.bestPrice.toString(),
                    color = Colors.Primary,
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
                )
            }
            // TODO: Add url
            Text(text = "@${product.bestStore.name}", color = Colors.TextDisabled)
        }
    }
}
