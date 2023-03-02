package app.adi_random.dealscraper.ui.productDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.data.models.UserProductInstalment
import app.adi_random.dealscraper.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val productRepository: ProductRepository,
    private val productId: Int
) : ViewModel() {
    private val _product = MutableStateFlow<ProductModel?>(null)
    val product = _product.asStateFlow()

    init{
        getProduct()
    }

    val productInstalmentsByOcrName =
        product.map { productModel ->
            productModel?.purchaseInstalments?.groupBy {
                it.ocrName
            }?.mapValues { (ocrName, instalmentsByOcrName) ->
                instalmentsByOcrName.groupBy { instalment ->
                    Pair(instalment.store, instalment.unitPrice)
                }.map { (key, instalmentsByStoreAndPrice) ->
                    UserProductInstalment(
                        qty = instalmentsByStoreAndPrice.sumOf { it.qty.toDouble() }.toFloat(),
                        unitPrice = key.second,
                        ocrName = ocrName,
                        store = key.first,
                        id = instalmentsByStoreAndPrice.first().id,
                        unitName = instalmentsByStoreAndPrice.first().unitName
                    )
                }
            }?.toList()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val actualSpending = product.map { productModel ->
        productModel?.purchaseInstalments?.sumOf { it.qty.toDouble() * it.unitPrice }?.toFloat()
            ?: 0f
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)

    private val bestSpending = product.map { productModel ->
        (productModel?.purchaseInstalments?.sumOf { instalment -> instalment.qty.toDouble() }
            ?.toFloat() ?: 0f) * (productModel?.bestPrice ?: 0f)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)

    val savings = actualSpending.combine(bestSpending) { actual, best ->
        actual - best
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)

    private fun getProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = productRepository.getProduct(productId)
            _product.value = res
        }
    }
}