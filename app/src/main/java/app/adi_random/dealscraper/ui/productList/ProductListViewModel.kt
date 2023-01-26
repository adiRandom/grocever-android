package app.adi_random.dealscraper.ui.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.data.repository.ProductRepository
import app.adi_random.dealscraper.data.repository.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductListViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    fun getProducts() {
        viewModelScope.launch {
            productRepository.getProductList().collect { result ->
                when (result) {
                    is ResultWrapper.Success -> _products.value = result.data
                    is ResultWrapper.Error -> TODO()
                    is ResultWrapper.Loading -> _isLoading.value = result.isLoading
                }
            }
        }
    }
}