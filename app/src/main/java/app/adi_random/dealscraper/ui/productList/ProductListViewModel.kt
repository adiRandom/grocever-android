package app.adi_random.dealscraper.ui.productList

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.data.models.ManualAddProductModel
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.data.models.StoreMetadataModel
import app.adi_random.dealscraper.data.repository.*
import app.adi_random.dealscraper.services.images.ImageDetectionService
import app.adi_random.dealscraper.services.images.ImageUploadService
import app.adi_random.dealscraper.usecase.ImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


typealias GalleryPermissionLauncher = ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>

class ProductListViewModel(
    private val productRepository: ProductRepository,
    private val uploadService: ImageUploadService,
    private val storeRepository: StoreRepository,
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    private val _navigateToProductDetails = MutableSharedFlow<String>()
    val navigateToProductDetails = _navigateToProductDetails.asSharedFlow()

    private val _hideKeyboard = MutableSharedFlow<Unit>()
    val hideKeyboard = _hideKeyboard.asSharedFlow()


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

    private val _shouldPickImageFromGallery = MutableSharedFlow<Unit>()
    val shouldPickImageFromGallery = _shouldPickImageFromGallery.asSharedFlow()

    private val _shouldCloseAddProductDialog = MutableSharedFlow<Unit>()
    val shouldCloseAddProductDialog = _shouldCloseAddProductDialog.asSharedFlow()

    private val _storeMetadata = MutableStateFlow<List<StoreMetadataModel>>(emptyList())
    val storeMetadata = _storeMetadata.asStateFlow()


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
                is ResultWrapper.Success -> _storeMetadata.value = result.data
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


    fun navigateToProductDetails(productName: String) {
        viewModelScope.launch {
            _navigateToProductDetails.emit(productName)
        }
    }

    fun addProduct(model: ManualAddProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.createProduct(model).collect() { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _shouldCloseAddProductDialog.emit(Unit)
                        // TODO: Show success message
                    }
                    is ResultWrapper.Error -> TODO()
                    is ResultWrapper.Loading -> _isLoading.value = result.isLoading
                }
            }
        }
    }

    fun pickImage() {
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

}