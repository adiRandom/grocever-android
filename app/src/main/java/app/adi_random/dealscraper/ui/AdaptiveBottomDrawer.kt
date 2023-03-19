package app.adi_random.dealscraper.ui

import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import app.adi_random.dealscraper.data.models.bottomSheet.AddProductBottomSheetModel
import app.adi_random.dealscraper.data.models.bottomSheet.BaseBottomSheetModel
import app.adi_random.dealscraper.data.models.bottomSheet.InfoMessageBottomSheetModel
import app.adi_random.dealscraper.data.models.bottomSheet.ReportBottomSheetModel
import app.adi_random.dealscraper.ui.misc.InfoBottomSheet
import app.adi_random.dealscraper.ui.report.ReportModalContent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AdaptiveBottomDrawer(
    drawerState: BottomDrawerState,
    model: BaseBottomSheetModel?,
    content: @Composable () -> Unit
) {
    BottomDrawer(drawerState = drawerState, drawerContent = {
        when (model) {
            is InfoMessageBottomSheetModel -> {
                InfoBottomSheet(model)
            }
            is AddProductBottomSheetModel -> {
                AddProductBottomDrawerContent(model)
            }
            is ReportBottomSheetModel -> {
                ReportModalContent(model)
            }
        }
    }) {
        content()
    }
}