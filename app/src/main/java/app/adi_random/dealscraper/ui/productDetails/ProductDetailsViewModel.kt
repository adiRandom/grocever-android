package app.adi_random.dealscraper.ui.productDetails

import androidx.core.util.rangeTo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.adi_random.dealscraper.data.models.*
import app.adi_random.dealscraper.data.models.bottomSheet.AddProductBottomSheetModel
import app.adi_random.dealscraper.data.models.bottomSheet.InfoMessageBottomSheetModel
import app.adi_random.dealscraper.data.models.bottomSheet.ReportBottomSheetModel
import app.adi_random.dealscraper.data.repository.ProductRepository
import app.adi_random.dealscraper.data.repository.ResultWrapper
import app.adi_random.dealscraper.data.repository.StoreRepository
import app.adi_random.dealscraper.ui.Constants
import app.adi_random.dealscraper.ui.misc.InfoStatus
import app.adi_random.dealscraper.ui.navigation.NavigationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val productRepository: ProductRepository,
    private val productId: Int,
    private val navigationViewModel: NavigationViewModel,
    private val storeRepository: StoreRepository
) : ViewModel() {
    private val _product = MutableStateFlow<ProductModel?>(null)
    val product = _product.asStateFlow()

    init{
        getStores()
    }

    private val storeMetadata = MutableStateFlow<List<StoreMetadataModel>>(emptyList())

    private val reportedProductLinks = MutableStateFlow<List<ReportMissLinkModel>>(emptyList())

    val reportableOrcProductNames = product.map { product ->
        (product?.purchaseInstalments ?: emptyList()).map { it.ocrName }
    }.combine(reportedProductLinks) { orcNames, missLinks ->
        orcNames.filter { ocrProductName ->
            missLinks.contains(ReportMissLinkModel(productId, ocrProductName)).not()
        }.toSet().toList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        getProduct()
        getReportableOrcProductNames()
    }

    val productInstalmentsByOcrName =
        product.map { productModel ->
            productModel?.purchaseInstalments?.groupBy {
                it.ocrName
            }?.mapValues { (ocrName, instalmentsByOcrName) ->
                instalmentsByOcrName.groupBy { instalment ->
                    Triple(instalment.store, instalment.unitPrice, instalment.date)
                }.map { (key, instalmentsByStoreAndPrice) ->
                    UserProductInstalment(
                        qty = instalmentsByStoreAndPrice.sumOf { it.qty.toDouble() }.toFloat(),
                        unitPrice = key.second,
                        ocrName = ocrName,
                        store = key.first,
                        id = instalmentsByStoreAndPrice.first().id,
                        unitName = instalmentsByStoreAndPrice.first().unitName,
                        date = key.third
                    )
                }
            }?.toList()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val instalmentsForSaving = product.map { product ->
        // Filter out the purchase instalments that are cheaper than the best price
        product?.purchaseInstalments?.filter { purchaseInstalment -> purchaseInstalment.unitPrice >= product.bestPrice }
    }

    private val actualSpending = product.map { product ->
        product?.purchaseInstalments?.fold(0f) { acc, purchaseInstalment -> acc + purchaseInstalment.qty * purchaseInstalment.unitPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)

    val savings =
        instalmentsForSaving.combine(actualSpending) { instalments, spent -> instalments to spent }
            .map { (purchaseInstalments, spent) ->
                val saves = (spent?:0f) - (product.value?.bestPrice ?: 0f)
                if (saves >0) saves else 0f
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)

    private fun getProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = productRepository.getProduct(productId)
            _product.value = res
        }
    }

    private fun getReportableOrcProductNames() {
        viewModelScope.launch(Dispatchers.IO) {
            reportedProductLinks.value = productRepository.getReportedProducts()
        }
    }

    private fun onReport(ocrProductName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.reportMissLink(productId, ocrProductName)
            reportedProductLinks.value =
                reportedProductLinks.value + ReportMissLinkModel(productId, ocrProductName)
        }
    }

    fun openReportBottomSheet() {
        navigationViewModel.showBottomSheet(
            ReportBottomSheetModel(
                reportableOrcProductNames.value,
                ::onReport
            )
        )
    }

    private fun onEdit(model: ManualAddProductModel) {
        (model as? EditPurchaseInstalmentModel)?.let { model ->
            viewModelScope.launch {
                try {
                    navigationViewModel.hideBottomSheet()
                    val newPurchaseInstalmentModel = productRepository.editProduct(model)
                    _product.value = _product.value?.copy(
                        purchaseInstalments = _product.value?.purchaseInstalments?.map {
                            if (it.id == model.id) {
                                newPurchaseInstalmentModel
                            } else {
                                it
                            }
                        } ?: emptyList()
                    )
                } catch (e: Exception) {
                    navigationViewModel.showBottomSheet(
                        InfoMessageBottomSheetModel(
                            e.message ?: "Unknown Error",
                            InfoStatus.ERROR
                        )
                    )
                }
            }
        }
    }

    fun onEditPurchaseInstalment(purchaseInstalment: UserProductInstalment) {
        navigationViewModel.showBottomSheet(
            AddProductBottomSheetModel(
                Constants.addProductUnitList,
                storeMetadata.value,
                EditPurchaseInstalmentModel(purchaseInstalment),
                ::onEdit,
                {}
            )
        )
    }

    private fun getStores() {
        viewModelScope.launch {
            storeRepository.getStore().collect() { result ->
                when (result) {
                    is ResultWrapper.Success -> storeMetadata.value = result.data
                    is ResultWrapper.Error -> TODO()
                    is ResultWrapper.Loading -> {
                    }
                }
            }
        }
    }
}