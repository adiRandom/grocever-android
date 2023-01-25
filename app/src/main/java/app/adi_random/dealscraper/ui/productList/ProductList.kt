package app.adi_random.dealscraper.ui.productList

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ProductList(viewModel: ProductListViewModel = koinViewModel(), navController: NavHostController) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        viewModel.getProducts()
    }

    LazyColumn() {
        items(items = products, key = { it.id }) {  product ->
            ProductEntry(product = product)
        }
    }
}