package app.adi_random.dealscraper.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.adi_random.dealscraper.data.repository.AuthRepository
import app.adi_random.dealscraper.data.repository.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NavigationViewModel(authRepository: AuthRepository) : ViewModel() {
    val startRoute =authRepository.isLoggedIn.map {
        if (it) Routes.PRODUCT_LIST else Routes.AUTH
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Routes.AUTH)
}