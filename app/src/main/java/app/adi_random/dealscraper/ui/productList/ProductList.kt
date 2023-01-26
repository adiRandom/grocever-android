package app.adi_random.dealscraper.ui.productList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import app.adi_random.dealscraper.ui.misc.LoadingScreen


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ProductList(
    viewModel: ProductListViewModel = koinViewModel(),
    navController: NavHostController
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.getProducts()
    }

    LoadingScreen(isLoading = isLoading) {
        Column() {
            val actualSpending by viewModel.actualSpending.collectAsStateWithLifecycle()
            val savings by viewModel.savings.collectAsStateWithLifecycle()
            val products by viewModel.products.collectAsStateWithLifecycle()
            ProductListHeader(actualSpending = actualSpending, savings = savings)
            LazyColumn(modifier = Modifier.padding(0.dp, 8.dp)) {
                items(items = products, key = { it.id }) { product ->
                    ProductEntry(product = product)
                }
            }
        }
    }
}