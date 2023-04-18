package app.adi_random.dealscraper.ui.weeklyOverview

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.ui.misc.LoadingScreen
import app.adi_random.dealscraper.ui.navigation.NavigationViewModel
import app.adi_random.dealscraper.ui.navigation.Routes
import app.adi_random.dealscraper.ui.productList.ProductEntry
import app.adi_random.dealscraper.usecase.CollectAsEffect
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun WeeklyOverviewProductList(
    viewModel: WeeklyProductListViewModel,
    navViewModel: NavigationViewModel,
    navController: NavHostController
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val scaffoldState = rememberScaffoldState()


    LaunchedEffect(true) {
        viewModel.loadInitialData()
    }

    viewModel.navigateToProductDetails.CollectAsEffect {
        navController.navigate(Routes.getProductDetailsRoute(it))
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
        ) {
            Column(modifier = Modifier.padding(it)) {
                val week by viewModel.currentWeek.collectAsStateWithLifecycle()
                val (startDate, endDate) = week
                val actualSpending by viewModel.actualSpending.collectAsStateWithLifecycle()
                val savings by viewModel.savings.collectAsStateWithLifecycle()
                val products by viewModel.products.collectAsStateWithLifecycle()
                WeeklyOverviewHeader(
                    actualSpending = actualSpending,
                    savings = savings,
                    startDate = startDate,
                    endDate = endDate
                )

                val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE - 1)
                val prevPage = remember {
                    mutableStateOf<Int>(pagerState.currentPage)
                }

                LaunchedEffect(pagerState.currentPage) {
                    val delta = pagerState.currentPage - (prevPage.value ?: 0)
                    if (delta != 0) {
                        viewModel.updateWeek(delta)
                    }
                    prevPage.value = pagerState.currentPage
                    }

                    HorizontalPager(pageCount = Int.MAX_VALUE, state = pagerState) { pageIndex ->
                        if (products.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = "No products bought this week",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(0.dp, 8.dp)
                                    .fillMaxHeight()
                            ) {
                                items(items = products, key = { item -> item.name }) { product ->
                                    ProductEntry(product = product) { id ->
                                        viewModel.navigateToProductDetails(id)
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
}