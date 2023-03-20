package app.adi_random.dealscraper.ui.navigation

import androidx.compose.material.BottomDrawerState
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.adi_random.dealscraper.data.models.bottomSheet.BaseBottomSheetModel
import app.adi_random.dealscraper.data.repository.AuthRepository
import app.adi_random.dealscraper.data.repository.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
class NavigationViewModel(authRepository: AuthRepository) : ViewModel() {
    val startRoute =authRepository.isLoggedIn.map {
        if (it) Routes.PRODUCT_LIST else Routes.AUTH
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Routes.AUTH)

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible = _isBottomSheetVisible.asStateFlow()

    private val _drawerState = mutableStateOf(BottomDrawerState(BottomDrawerValue.Closed))
    val drawerState: State<BottomDrawerState> = _drawerState

    private val _bottomSheetModel = MutableStateFlow<BaseBottomSheetModel?>(null)
    val bottomSheetModel: StateFlow<BaseBottomSheetModel?> = _bottomSheetModel

    fun showBottomSheet(model: BaseBottomSheetModel) {
        viewModelScope.launch {
            _bottomSheetModel.value = model
            _isBottomSheetVisible.value = true
        }
    }

    fun hideBottomSheet() {
        viewModelScope.launch {
            _bottomSheetModel.value = null
            _isBottomSheetVisible.value = false
        }
    }
}