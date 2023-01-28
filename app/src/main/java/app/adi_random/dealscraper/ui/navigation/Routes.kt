package app.adi_random.dealscraper.ui.navigation

object Routes {
    const val AUTH = "auth"
    const val PRODUCT_LIST = "productList"

    private const val PRODUCT_NAME_ARG = "productName"
    const val PRODUCT_DETAILS = "productDetails/{$PRODUCT_NAME_ARG}"

    private fun String.replaceArgWithValue(arg: String, value: String): String {
        return this.replace("{$arg}", value)
    }

    fun getProductDetailsRoute(productName: String) =
        PRODUCT_DETAILS.replaceArgWithValue(PRODUCT_NAME_ARG, productName)
}