package app.adi_random.dealscraper.ui.navigation

object Routes {
    const val AUTH = "auth"
    const val PRODUCT_LIST = "productList"

    const val PRODUCT_ID_ARG = "productId"
    const val PRODUCT_DETAILS = "productDetails/{$PRODUCT_ID_ARG}"
    const val WEEKLY_OVERVIEW = "weeklyOverview"

    private fun String.replaceArgWithValue(arg: String, value: String): String {
        return this.replace("{$arg}", value)
    }

    fun getProductDetailsRoute(productId: Int) =
        PRODUCT_DETAILS.replaceArgWithValue(PRODUCT_ID_ARG, productId.toString())
}