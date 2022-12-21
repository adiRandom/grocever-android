package app.adi_random.dealscraper.ui.productList

import androidx.lifecycle.ViewModel
import app.adi_random.dealscraper.data.models.ProductModel
import kotlinx.coroutines.flow.MutableStateFlow

class ProductListViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products = _products

    fun getProducts() {
        // TODO: Make api call to get products
        _products.value = listOf(
            ProductModel(
                id = 1,
                name = "Product 1",
                ocrName = "Product 1",
                price = 10.0f,
                bestPrice = 5.0f,
                url = "https://www.google.com",
                store = "Store 1"
            ),
            ProductModel(
                id = 2,
                name = "Product 2",
                ocrName = "Product 2",
                price = 20.0f,
                bestPrice = 15.0f,
                url = "https://www.google.com",
                store = "Store 2"
            ),
            ProductModel(
                id = 3,
                name = "Product 3",
                ocrName = "Product 3",
                price = 30.0f,
                bestPrice = 25.0f,
                url = "https://www.google.com",
                store = "Store 3"
            )
        )
    }
}