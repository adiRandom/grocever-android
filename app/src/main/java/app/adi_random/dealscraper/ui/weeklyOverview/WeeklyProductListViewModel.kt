package app.adi_random.dealscraper.ui.weeklyOverview

import androidx.lifecycle.*
import app.adi_random.dealscraper.data.models.ManualAddProductModel
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.data.models.StoreMetadataModel
import app.adi_random.dealscraper.data.models.bottomSheet.AddProductBottomSheetModel
import app.adi_random.dealscraper.data.models.bottomSheet.InfoMessageBottomSheetModel
import app.adi_random.dealscraper.data.repository.*
import app.adi_random.dealscraper.ui.navigation.NavigationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class WeeklyProductListViewModel(
    private val productRepository: ProductRepository,
    private val storeRepository: StoreRepository,
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    private val _navigateToProductDetails = MutableSharedFlow<Int>()
    val navigateToProductDetails = _navigateToProductDetails.asSharedFlow()

    private val _hideKeyboard = MutableSharedFlow<Unit>()
    val hideKeyboard = _hideKeyboard.asSharedFlow()

    private val _currentWeek = MutableStateFlow(getInitialWeek())
    val currentWeek = _currentWeek.asStateFlow()


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

    private val storeMetadata = MutableStateFlow<List<StoreMetadataModel>>(emptyList())


    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val products = productRepository.getProductListBetweenDates(
                currentWeek.value.first,
                currentWeek.value.second
            )
            _products.value = products
        }
    }

    private suspend fun getStores() {
        storeRepository.getStore().collect() { result ->
            when (result) {
                is ResultWrapper.Success -> storeMetadata.value = result.data
                is ResultWrapper.Error -> TODO()
                is ResultWrapper.Loading -> _isLoading.value = result.isLoading
            }
        }
    }

    fun loadInitialData() {
        viewModelScope.launch(Dispatchers.IO) {
            getProducts()
            getStores()
        }
    }


    fun onDrawerOpenStateChange(isOpen: Boolean) {
        viewModelScope.launch {
            if (isOpen.not()) {
                _hideKeyboard.emit(Unit)
            }
        }
    }


    fun navigateToProductDetails(productId: Int) {
        viewModelScope.launch {
            _navigateToProductDetails.emit(productId)
        }
    }

    private fun getInitialWeek(): Pair<Long, Long>{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val startOfWeek = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val endOfWeek = calendar.timeInMillis
        return Pair(startOfWeek, endOfWeek)
    }

    fun updateWeek(weekIncrement: Int) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentWeek.value.first
        calendar.add(Calendar.DAY_OF_WEEK, weekIncrement * 7)
        val startOfWeek = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val endOfWeek = calendar.timeInMillis
        _currentWeek.value = Pair(startOfWeek, endOfWeek)

        getProducts()
    }
}