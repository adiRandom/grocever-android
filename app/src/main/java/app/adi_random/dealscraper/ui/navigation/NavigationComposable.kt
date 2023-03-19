package app.adi_random.dealscraper.ui.navigation

import androidx.compose.material.BottomDrawer
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.adi_random.dealscraper.ui.AdaptiveBottomDrawer
import app.adi_random.dealscraper.ui.AddProductBottomDrawerContent
import app.adi_random.dealscraper.ui.auth.AuthScreen
import app.adi_random.dealscraper.ui.misc.InfoBottomSheet
import app.adi_random.dealscraper.ui.productDetails.ProductDetails
import app.adi_random.dealscraper.ui.productList.ProductList
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterialApi::class)

fun Navigation(viewModel: NavigationViewModel = koinViewModel()) {
    val navController = rememberNavController()
    val startDestination by viewModel.startRoute.collectAsStateWithLifecycle()
    val bottomSheetModel by viewModel.bottomSheetModel.collectAsStateWithLifecycle()

    AdaptiveBottomDrawer(drawerState = viewModel.drawerState.value, model = bottomSheetModel) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable(Routes.AUTH) {
                AuthScreen(navController = navController)
            }
            composable(Routes.PRODUCT_LIST) {
                ProductList(
                    viewModel = getViewModel() {
                        parametersOf(viewModel)
                    }, navController = navController,
                    navViewModel = viewModel
                )
            }
            composable(
                Routes.PRODUCT_DETAILS,
                arguments = listOf(navArgument(Routes.PRODUCT_ID_ARG) { type = NavType.IntType })
            )
            {
                val productId = it.arguments?.getInt(Routes.PRODUCT_ID_ARG)
                ProductDetails(viewModel = getViewModel { parametersOf(productId, viewModel) })
            }
        }
    }
}