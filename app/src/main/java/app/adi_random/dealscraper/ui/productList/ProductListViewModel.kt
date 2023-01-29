package app.adi_random.dealscraper.ui.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.data.models.ManualAddProductModel
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

    private val _addProductName = MutableStateFlow("")
    val addProductName = _addProductName.asStateFlow()

    private val _addProductUnitPrice = MutableStateFlow(0f)
    val addProductUnitPrice = _addProductUnitPrice.map { it.toString() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    private val _addProductQuantity = MutableStateFlow(0f)
    val addProductQuantity = _addProductQuantity.map { it.toString() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    private val _addProductUnit = MutableStateFlow(R.string.manually_add_unit_buc)
    val addProductUnit = _addProductUnit.asStateFlow()

    private val _hideKeyboard = MutableSharedFlow<Unit>()
    val hideKeyboard = _hideKeyboard.asSharedFlow()

    private val _isAddProductUnitDropdownOpen = MutableStateFlow(false)
    val isAddProductUnitDropdownOpen = _isAddProductUnitDropdownOpen.asStateFlow()

    val addProductUnitList = listOf(R.string.manually_add_unit_buc, R.string.manually_add_unit_kg)

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

    fun setAddProductName(name: String) {
        _addProductName.value = name
    }

    fun setAddProductUnitPrice(price: String) {
        _addProductUnitPrice.value = price.toFloat()
    }

    fun setAddProductQuantity(quantity: String) {
        _addProductQuantity.value = quantity.toFloat()
    }

    fun setAddProductUnit(unitStringRes: Int) {
        _addProductUnit.value = unitStringRes
    }

    fun onDrawerOpenStateChange(isOpen: Boolean) {
        viewModelScope.launch {
            if (isOpen.not()) {
                _hideKeyboard.emit(Unit)
            }
        }
    }

    fun setIsAddProductUnitDropdownOpen(isOpen: Boolean) {
        _isAddProductUnitDropdownOpen.value = isOpen
    }

    fun navigateToProductDetails(productName: String) {
        viewModelScope.launch {
            _navigateToProductDetails.emit(productName)
        }
    }

    fun addProduct(model: ManualAddProductModel){
        //TODO: Call repository to add product
    }
}