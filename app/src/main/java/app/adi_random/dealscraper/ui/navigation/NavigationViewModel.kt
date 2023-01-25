package app.adi_random.dealscraper.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.adi_random.dealscraper.data.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NavigationViewModel(authRepository: AuthRepository) : ViewModel() {
    val startRoute = authRepository.isLoggedIn().map {
        if (it) Routes.PRODUCT_LIST else Routes.AUTH
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Routes.AUTH)
}