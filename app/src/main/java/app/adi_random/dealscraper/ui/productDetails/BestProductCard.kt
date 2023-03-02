package app.adi_random.dealscraper.ui.productDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.ui.text.HyperlinkText
import app.adi_random.dealscraper.ui.theme.Colors
import coil.compose.AsyncImage

@Composable
fun BestProductCard(product: ProductModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 16.dp)
            .background(color = Colors.Background, shape = RoundedCornerShape(8.dp))
            .padding(16.dp, 8.dp)
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = "product image",
            modifier = Modifier.size(96.dp)
        )
        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
        ) {
            Text(text = product.name, color = Colors.Secondary)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 24.dp, 0.dp, 4.dp)
            ) {
                Text(
                    text = "${product.bestPrice} RON / ${product.purchaseInstalments[0].unitName}",
                    color = Colors.TextPrimary
                )
                HyperlinkText(text = product.bestStore.name, link = product.url)
            }
        }
    }
}
