package app.adi_random.dealscraper.ui.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.ui.images.GifLoader
import app.adi_random.dealscraper.usecase.dpToPx
import coil.size.Dimension
import coil.size.Size

@Composable
fun LoadingScreen(isLoading: Boolean, content: @Composable () -> Unit) {
    content()
    if (isLoading) {
        LoadingDialog()
    }
}

@Composable
private fun LoadingDialog() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.3f))
    ) {
        GifLoader(drawableRes = R.drawable.loading_spinner, modifier = Modifier.width(128.dp))
    }
}