package app.adi_random.dealscraper.ui.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.data.repository.ProductRepository
import app.adi_random.dealscraper.data.repository.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductListViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    private val _navigateToProductDetails = MutableSharedFlow<String>()
    val navigateToProductDetails = _navigateToProductDetails.asSharedFlow()

    val actualSpending = products.map { products ->
        products.map { product ->
            product.purchaseInstalments.fold(0f) { acc, purchaseInstalment -> acc + purchaseInstalment.qty * purchaseInstalment.unitPrice }
        }.sum()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)

    private val bestSpending = products.map { products ->
        products.map { product ->
            product.bestPrice
        }.sum()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)

    val savings = actualSpending.combine(bestSpending) { actual, best ->
        actual - best
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getProductList().collect { result ->
                when (result) {
                    is ResultWrapper.Success -> _products.value = result.data
                    is ResultWrapper.Error -> TODO()
                    is ResultWrapper.Loading -> _isLoading.value = result.isLoading
                }
            }
        }
    }

    fun navigateToProductDetails(productName: String) {
        viewModelScope.launch {
            _navigateToProductDetails.emit(productName)
        }
    }
}