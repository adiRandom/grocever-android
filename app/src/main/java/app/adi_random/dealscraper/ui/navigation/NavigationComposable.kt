package app.adi_random.dealscraper.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.adi_random.dealscraper.ui.auth.AuthScreen
import app.adi_random.dealscraper.ui.productDetails.ProductDetails
import app.adi_random.dealscraper.ui.productList.ProductList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
@OptIn(ExperimentalLifecycleComposeApi::class)

fun Navigation(viewModel: NavigationViewModel = koinViewModel()) {
    val navController = rememberNavController()
    val startDestination by viewModel.startRoute.collectAsStateWithLifecycle()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.AUTH) {
            AuthScreen(navController = navController)
        }
        composable(Routes.PRODUCT_LIST) {
            ProductList(navController = navController)
        }
        composable(Routes.PRODUCT_DETAILS) {
            val productName = it.arguments?.getString("productName")
            ProductDetails(viewModel = koinViewModel { parametersOf(productName) })
        }
    }
}