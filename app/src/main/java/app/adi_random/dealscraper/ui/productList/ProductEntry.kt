package app.adi_random.dealscraper.ui.productList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.ui.text.HyperlinkText
import app.adi_random.dealscraper.ui.theme.Colors
import coil.compose.AsyncImage
import kotlin.math.roundToInt

@Composable
fun ProductEntry(product: ProductModel, openProductDetails: (Int) -> Unit) {
    val worstPrice = product.purchaseInstalments.fold(0f) { acc, instalment ->
        val price = instalment.qty * instalment.unitPrice
        if (price > acc) {
            price
        } else {
            acc
        }
    }

    val discountPercentage =
        ((worstPrice - product.bestPrice) / worstPrice * 100).roundToInt()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Colors.Background)
            .clickable {
                openProductDetails(product.id)
            }
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = "product image",
            modifier = Modifier.size(96.dp)
        )
        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
        ) {
            Row {
                Text(text = product.name, color = Colors.TextPrimary)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 0.dp)
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    if (worstPrice > product.bestPrice) {
                        Text(
                            text = "$worstPrice RON",
                            color = Colors.TextSecondary,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Text(
                            text = product.bestPrice.toString() + " RON",
                            color = Colors.Primary,
                            modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
                        )
                        Text(
                            text = "(-$discountPercentage%)",
                            color = Colors.Discount,
                            modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
                            fontSize = 12.sp
                        )
                    } else {
                        Text(
                            text = "$worstPrice RON",
                            color = Colors.TextSecondary,
                        )
                    }

                }
                HyperlinkText(text = "@${product.bestStore.name}", link = product.url)
            }
        }
    }
}
