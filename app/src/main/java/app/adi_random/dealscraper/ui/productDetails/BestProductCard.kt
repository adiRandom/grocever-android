package app.adi_random.dealscraper.ui.productDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.ui.theme.Colors

@Composable
fun BestProductCard(product: ProductModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 16.dp)
            .background(color = Colors.Background, shape = RoundedCornerShape(8.dp))
            .padding(16.dp, 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 4.dp)
        ) {
            Text(text = product.name, color = Colors.Secondary)
            // TODO: Add Url
            Text(text = product.bestStore.name, color = Colors.TextPrimary)
        }
        Text(text = "${product.bestPrice} RON / ${product.purchaseInstalments[0].unitName}", color = Colors.TextPrimary)
    }
}
