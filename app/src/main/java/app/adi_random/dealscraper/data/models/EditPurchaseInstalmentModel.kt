package app.adi_random.dealscraper.data.models

class EditPurchaseInstalmentModel(
    name: String,
    unitPrice: Float,
    quantity: Float,
    unitName: String,
    storeId: Int,
    date: Long?,
    val id: Int,
) : ManualAddProductModel(name, unitPrice, quantity, unitName, storeId, date) {
    constructor(instalment: UserProductInstalment) : this(
        instalment.ocrName,
        instalment.unitPrice,
        instalment.qty,
        instalment.unitName,
        instalment.store.id,
        instalment.date,
        instalment.id
    )
}