package app.adi_random.dealscraper.data.models.bottomSheet

import app.adi_random.dealscraper.data.models.ManualAddProductModel
import app.adi_random.dealscraper.data.models.StoreMetadataModel

data class AddProductBottomSheetModel(
    val unitStringResList: List<Int> = emptyList(),
    val stores: List<StoreMetadataModel> = emptyList(),
    val onSubmit: (model: ManualAddProductModel) -> Unit,
    val pickImage: () -> Unit
):BaseBottomSheetModel
