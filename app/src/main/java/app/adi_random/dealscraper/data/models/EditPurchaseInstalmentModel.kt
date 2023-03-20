package app.adi_random.dealscraper.data.models

class EditPurchaseInstalmentModel(
    name: String,
    unitPrice: Float,
    quantity: Float,
    unitName: String,
    storeId: Int,
    val id: Int,
) : ManualAddProductModel(name, unitPrice, quantity, unitName, storeId) {
    constructor(instalment: UserProductInstalment) : this(
        instalment.ocrName,
        instalment.unitPrice,
        instalment.qty,
        instalment.unitName,
        instalment.store.id,
        instalment.id
    )
}