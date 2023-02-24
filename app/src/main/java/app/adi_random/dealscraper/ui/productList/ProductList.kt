package app.adi_random.dealscraper.ui.productList

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.ui.AddProductBottomDrawerContent
import app.adi_random.dealscraper.ui.misc.LoadingScreen
import app.adi_random.dealscraper.ui.navigation.Routes
import app.adi_random.dealscraper.usecase.CollectAsEffect
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalLifecycleComposeApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ProductList(
    viewModel: ProductListViewModel = koinViewModel(),
    navController: NavHostController
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            viewModel.onImagePicked(uri, context)
        }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                viewModel.startUploadService()
            } else {
               ( context as? Activity)?.finish()
            }
        }


    LaunchedEffect(true) {
        viewModel.loadInitialData()
    }

    viewModel.navigateToProductDetails.CollectAsEffect {
        navController.navigate(Routes.getProductDetailsRoute(it))
    }


    viewModel.shouldPickImageFromGallery.CollectAsEffect {
        galleryLauncher.launch("image/*")
    }

    viewModel.shouldCloseAddProductDialog.CollectAsEffect {
        scope.launch {
            drawerState.close()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.requestGalleryPermission(context, requestPermissionLauncher)
    }


    LoadingScreen(isLoading = isLoading) {

        val keyboardController = LocalSoftwareKeyboardController.current
        LaunchedEffect(drawerState.isOpen) {
            viewModel.onDrawerOpenStateChange(drawerState.isOpen)
        }
        viewModel.hideKeyboard.CollectAsEffect {
            keyboardController?.hide()
        }

        val stores by viewModel.storeMetadata.collectAsStateWithLifecycle()

        BottomDrawer(drawerState = drawerState, drawerContent = {
            AddProductBottomDrawerContent(
                unitStringResList = viewModel.addProductUnitList,
                stores = stores,
                onSubmit = viewModel::addProduct, pickImage = viewModel::pickImage
            )
        }) {
            Scaffold(
                scaffoldState = scaffoldState,
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = "Menu"
                        )
                    }
                },

                ) {
                Column() {
                    val actualSpending by viewModel.actualSpending.collectAsStateWithLifecycle()
                    val savings by viewModel.savings.collectAsStateWithLifecycle()
                    val products by viewModel.products.collectAsStateWithLifecycle()
                    ProductListHeader(actualSpending = actualSpending, savings = savings)
                    LazyColumn(modifier = Modifier.padding(0.dp, 8.dp)) {
                        items(items = products, key = { it.name }) { product ->
                            ProductEntry(product = product) {
                                viewModel.navigateToProductDetails(it)
                            }
                        }
                    }
                }
            }
        }
    }
}