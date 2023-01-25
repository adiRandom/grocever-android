package app.adi_random.dealscraper.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.adi_random.dealscraper.ui.auth.AuthScreen
import app.adi_random.dealscraper.ui.productList.ProductList


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "auth") {
        composable(Routes.AUTH) {
            AuthScreen(navController = navController)
        }
        composable(Routes.PRODUCT_LIST) {
            ProductList(navController = navController)
        }
    }
}