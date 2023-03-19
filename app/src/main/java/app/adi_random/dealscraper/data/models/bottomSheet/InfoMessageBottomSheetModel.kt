package app.adi_random.dealscraper.data.models.bottomSheet

import app.adi_random.dealscraper.ui.misc.InfoStatus

data class InfoMessageBottomSheetModel(
   val message: String,
   val status: InfoStatus
):BaseBottomSheetModel