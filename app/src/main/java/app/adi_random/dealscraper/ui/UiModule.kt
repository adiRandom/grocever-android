package app.adi_random.dealscraper.ui

import app.adi_random.dealscraper.ui.auth.AuthScreenViewModel
import app.adi_random.dealscraper.ui.navigation.NavigationViewModel
import app.adi_random.dealscraper.ui.productDetails.ProductDetailsViewModel
import app.adi_random.dealscraper.ui.productList.ProductListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        ProductListViewModel(
            get(), get(), storeRepository = get(), authRepository = get()
        )
    }
    viewModel { AuthScreenViewModel(get(), get()) }
    viewModel { NavigationViewModel(get()) }
    viewModel { (productId: Int) ->
        ProductDetailsViewModel(
            productRepository = get(),
            productId = productId,
        )
    }
}