package app.adi_random.dealscraper.ui

import app.adi_random.dealscraper.data.models.StoreMetadataModel
import app.adi_random.dealscraper.ui.auth.AuthScreenViewModel
import app.adi_random.dealscraper.ui.navigation.NavigationViewModel
import app.adi_random.dealscraper.ui.productDetails.ProductDetailsViewModel
import app.adi_random.dealscraper.ui.productList.ProductListViewModel
import app.adi_random.dealscraper.ui.weeklyOverview.WeeklyProductListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { (navViewModel: NavigationViewModel) ->
        ProductListViewModel(
            get(), get(), storeRepository = get(), authRepository = get(), navViewModel
        )
    }
    viewModel { AuthScreenViewModel(get(), get()) }
    viewModel { NavigationViewModel(get()) }
    viewModel { (productId: Int,
                    navigationViewModel: NavigationViewModel,
                ) ->
        ProductDetailsViewModel(
            productRepository = get(),
            productId = productId,
            navigationViewModel = navigationViewModel,
            storeRepository = get(),
        )
    }

    viewModel{
        WeeklyProductListViewModel(
            productRepository = get(),
            storeRepository = get(),
        )
    }
}