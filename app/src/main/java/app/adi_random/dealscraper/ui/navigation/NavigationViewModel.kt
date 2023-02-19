package app.adi_random.dealscraper.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.adi_random.dealscraper.data.repository.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NavigationViewModel(preferencesRepository: PreferencesRepository) : ViewModel() {
    val startRoute =
        MutableStateFlow(if (preferencesRepository.getToken() != null) Routes.PRODUCT_LIST else Routes.AUTH).asStateFlow()
}