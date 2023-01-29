package app.adi_random.dealscraper.ui.productList

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.ui.AddProductBottomDrawerContent
import app.adi_random.dealscraper.ui.misc.LoadingScreen
import app.adi_random.dealscraper.ui.navigation.Routes
import app.adi_random.dealscraper.usecase.CollectAsEffect
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalLifecycleComposeApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ProductList(
    viewModel: ProductListViewModel = koinViewModel(),
    navController: NavHostController
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        viewModel.getProducts()
    }

    viewModel.navigateToProductDetails.CollectAsEffect {
        navController.navigate(Routes.getProductDetailsRoute(it))
    }


    LoadingScreen(isLoading = isLoading) {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

        val keyboardController = LocalSoftwareKeyboardController.current
        LaunchedEffect(drawerState.isOpen) {
            viewModel.onDrawerOpenStateChange(drawerState.isOpen)
        }
        viewModel.hideKeyboard.CollectAsEffect {
            keyboardController?.hide()
        }

        BottomDrawer(drawerState = drawerState, drawerContent = {
            AddProductBottomDrawerContent(unitStringResList = viewModel.addProductUnitList, onSubmit = viewModel::addProduct)
        }) {
            Scaffold(
                scaffoldState = scaffoldState,
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = "Menu"
                        )
                    }
                },

                ) {
                Column() {
                    val actualSpending by viewModel.actualSpending.collectAsStateWithLifecycle()
                    val savings by viewModel.savings.collectAsStateWithLifecycle()
                    val products by viewModel.products.collectAsStateWithLifecycle()
                    ProductListHeader(actualSpending = actualSpending, savings = savings)
                    LazyColumn(modifier = Modifier.padding(0.dp, 8.dp)) {
                        items(items = products, key = { it.name }) { product ->
                            ProductEntry(product = product) {
                                viewModel.navigateToProductDetails(it)
                            }
                        }
                    }
                }
            }
        }
    }
}