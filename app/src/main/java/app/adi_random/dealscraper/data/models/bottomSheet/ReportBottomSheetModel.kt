package app.adi_random.dealscraper.data.models.bottomSheet

data class ReportBottomSheetModel(
    val ocrProductNames: List<String>,
    val onReport: (String) -> Unit
):BaseBottomSheetModel
