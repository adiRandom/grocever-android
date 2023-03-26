package app.adi_random.dealscraper.ui.productList

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.data.models.bottomSheet.AddProductBottomSheetModel
import app.adi_random.dealscraper.data.models.bottomSheet.InfoMessageBottomSheetModel
import app.adi_random.dealscraper.ui.misc.LoadingScreen
import app.adi_random.dealscraper.ui.navigation.NavigationViewModel
import app.adi_random.dealscraper.ui.navigation.Routes
import app.adi_random.dealscraper.usecase.CollectAsEffect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(
    ExperimentalLifecycleComposeApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ProductList(
    viewModel: ProductListViewModel,
    navViewModel: NavigationViewModel,
    navController: NavHostController
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
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

    viewModel.isBottomDrawerOpen.CollectAsEffect {
        if (it) {
            viewModel.openBottomDrawer()
        } else {
            navViewModel.hideBottomSheet()
        }
    }


    viewModel.shouldPickImageFromGallery.CollectAsEffect {
        galleryLauncher.launch("image/*")
    }

    viewModel.shouldCloseAddProductDialog.CollectAsEffect {
        scope.launch {
            navViewModel.hideBottomSheet()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.requestGalleryPermission(context, requestPermissionLauncher)
    }

    LoadingScreen(isLoading = isLoading) {

        val keyboardController = LocalSoftwareKeyboardController.current
        LaunchedEffect(navViewModel.drawerState.value.isVisible) {
            viewModel.onDrawerOpenStateChange(navViewModel.drawerState.value.isVisible)
        }
        viewModel.hideKeyboard.CollectAsEffect {
            keyboardController?.hide()
        }


        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.openBottomDrawer()
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
                ProductListHeader(actualSpending = actualSpending, savings = savings) {
                    viewModel.logout()
                }

                if (products.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "No products added yet",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
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