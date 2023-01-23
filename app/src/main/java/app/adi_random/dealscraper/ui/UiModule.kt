package app.adi_random.dealscraper.ui

import app.adi_random.dealscraper.ui.auth.AuthScreenViewModel
import app.adi_random.dealscraper.ui.productList.ProductListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { ProductListViewModel() }
    viewModel { AuthScreenViewModel(get()) }
}