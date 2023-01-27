package app.adi_random.dealscraper.ui.productDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.adi_random.dealscraper.data.models.UserProductInstalment
import app.adi_random.dealscraper.data.repository.ProductRepository
import kotlinx.coroutines.flow.*

class ProductDetailsViewModel(productRepository: ProductRepository, productId: Int) : ViewModel() {
    val product = MutableStateFlow(productRepository.getProduct(productId)).asStateFlow()
    val productInstalmentsByOcrName =
        product.map { productModel ->
            productModel?.purchaseInstalments?.groupBy {
                it.ocrName
            }?.mapValues { (ocrName, instalments) ->
                instalments.groupBy { instalment ->
                    Pair(instalment.storeName, instalment.unitPrice)
                }.map { (key, instalmentsByNameAndPrice) ->
                    UserProductInstalment(
                        qty = instalmentsByNameAndPrice.sumOf { it.qty.toDouble() }.toFloat(),
                        unitPrice = key.second,
                        ocrName = ocrName,
                        storeName = key.first
                    )
                }
            }?.toList()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val actualSpending = product.map { productModel ->
        productModel?.purchaseInstalments?.sumOf { it.qty.toDouble() * it.unitPrice }?.toFloat()
            ?: 0f
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)

    private val bestSpending = product.map { productModel ->
        (productModel?.purchaseInstalments?.sumOf { instalment -> instalment.unitPrice.toDouble() }
            ?.toFloat() ?: 0f) * (productModel?.bestPrice ?: 0f)
    }

    val savings = actualSpending.combine(bestSpending) { actual, best ->
        actual - best
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)
}