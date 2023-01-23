package app.adi_random.dealscraper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import app.adi_random.dealscraper.ui.auth.AuthScreen
import app.adi_random.dealscraper.ui.productList.ProductList
import app.adi_random.dealscraper.ui.theme.Colors
import app.adi_random.dealscraper.ui.theme.DealScraperTheme

class MainComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DealScraperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Colors.Background
                ) {
                    AuthScreen()
                }
            }
        }
    }
}