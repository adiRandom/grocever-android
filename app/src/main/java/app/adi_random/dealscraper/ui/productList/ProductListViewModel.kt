package app.adi_random.dealscraper.ui.productList

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.data.models.ManualAddProductModel
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.data.models.StoreMetadataModel
import app.adi_random.dealscraper.data.models.bottomSheet.AddProductBottomSheetModel
import app.adi_random.dealscraper.data.models.bottomSheet.InfoMessageBottomSheetModel
import app.adi_random.dealscraper.data.repository.*
import app.adi_random.dealscraper.services.images.ImageDetectionService
import app.adi_random.dealscraper.services.images.ImageUploadService
import app.adi_random.dealscraper.ui.Constants
import app.adi_random.dealscraper.ui.misc.InfoStatus
import app.adi_random.dealscraper.ui.navigation.NavigationViewModel
import app.adi_random.dealscraper.usecase.ImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


typealias GalleryPermissionLauncher = ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>

class ProductListViewModel(
    private val productRepository: ProductRepository,
    private val uploadService: ImageUploadService,
    private val storeRepository: StoreRepository,
    private val authRepository: AuthRepository,
    private val navigationViewModel: NavigationViewModel
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    private val _navigateToProductDetails = MutableSharedFlow<Int>()
    val navigateToProductDetails = _navigateToProductDetails.asSharedFlow()

    private val _hideKeyboard = MutableSharedFlow<Unit>()
    val hideKeyboard = _hideKeyboard.asSharedFlow()

    private val _navigateToWeeklyOverview = MutableSharedFlow<Unit>()
    val navigateToWeeklyOverview = _navigateToWeeklyOverview.asSharedFlow()

    // Ignore in the savings calculation the products that have a best_product worse than what the user paid
    private val productsForSavings = products.map{products ->
        products.map{product->
            // Filter out the purchase instalments that are cheaper than the best price
            val filteredPurchaseInstalments = product.purchaseInstalments.filter { purchaseInstalment -> purchaseInstalment.unitPrice >= product.bestPrice }
            product.copy(purchaseInstalments = filteredPurchaseInstalments)
        }.filter { product -> product.purchaseInstalments.isNotEmpty() }
    }

    val actualSpending = products.map { products ->
        products.map { product ->
            product.purchaseInstalments.fold(0f) { acc, purchaseInstalment -> acc + purchaseInstalment.qty * purchaseInstalment.unitPrice }
        }.sum()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)

    val savings = productsForSavings.map { products ->
        products.map { product ->
            val spent = product.purchaseInstalments.fold(0f) { acc, purchaseInstalment ->
                acc + purchaseInstalment.qty * purchaseInstalment.unitPrice
            }
            spent - product.bestPrice
        }.sum()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)

    private val _shouldPickImageFromGallery = MutableSharedFlow<Unit>()
    val shouldPickImageFromGallery = _shouldPickImageFromGallery.asSharedFlow()

    private val _shouldCloseAddProductDialog = MutableSharedFlow<Unit>()
    val shouldCloseAddProductDialog = _shouldCloseAddProductDialog.asSharedFlow()

    private val storeMetadata = MutableStateFlow<List<StoreMetadataModel>>(emptyList())

    private val _isBottomDrawerOpen = MutableSharedFlow<Boolean>()
    val isBottomDrawerOpen = _isBottomDrawerOpen.asSharedFlow()


    private val infoMessage = MutableStateFlow("" to InfoStatus.SUCCESS)

    private var didRequestGalleryPermission = false

    private suspend fun getProducts() {
        productRepository.getProductList().collect { result ->
            when (result) {
                is ResultWrapper.Success -> _products.value = result.data
                is ResultWrapper.Error -> TODO()
                is ResultWrapper.Loading -> _isLoading.value = result.isLoading
            }
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

    private fun addProduct(model: ManualAddProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.createProduct(model).collect() { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _shouldCloseAddProductDialog.emit(Unit)
                        showInfoMessage( "Product added. We'll work our magic to find you the best deal" to InfoStatus.SUCCESS)
                    }
                    is ResultWrapper.Error -> TODO()
                    is ResultWrapper.Loading -> _isLoading.value = result.isLoading
                }
            }
        }
    }

    private fun pickImage() {
        viewModelScope.launch {
            _shouldPickImageFromGallery.emit(Unit)
        }
    }

    fun onImagePicked(uri: Uri?, context: Context) {
        viewModelScope.launch {
            uploadService.uploadImage(uri, context).collect { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _shouldCloseAddProductDialog.emit(Unit)
                    }
                    is ResultWrapper.Error -> TODO()
                    is ResultWrapper.Loading -> _isLoading.value = result.isLoading
                }
            }
        }
    }

    fun startUploadService() {
        viewModelScope.launch {
            uploadService.startService()
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.logout()
        }
    }

    fun requestGalleryPermission(
        ctx: Context,
        launcher: GalleryPermissionLauncher
    ) {
        if(didRequestGalleryPermission){
            return
        }

        didRequestGalleryPermission = true

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                startUploadService()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                launcher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    private fun showInfoMessage(message: Pair<String, InfoStatus>) {
        viewModelScope.launch {
            infoMessage.emit(message)
            _isBottomDrawerOpen.emit(true)
            delay(3000)
            _isBottomDrawerOpen.emit(false)
            delay(500)
            infoMessage.emit("" to InfoStatus.SUCCESS)
        }
    }

    fun openBottomDrawer() {
        viewModelScope.launch {
            navigationViewModel.showBottomSheet(
                if (infoMessage.value.first != "") {
                    InfoMessageBottomSheetModel(
                        message = infoMessage.value.first,
                        status = infoMessage.value.second
                    )
                } else {
                    AddProductBottomSheetModel(
                        unitStringResList = Constants.addProductUnitList,
                        stores = storeMetadata.value,
                        onSubmit = ::addProduct,
                        pickImage = ::pickImage
                    )
                }
            )
        }
    }

    fun onWeeklyOverviewClicked() {
        viewModelScope.launch {
            _navigateToWeeklyOverview.emit(Unit)
        }
    }
}