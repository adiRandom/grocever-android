package app.adi_random.dealscraper.data.dto.product

import app.adi_random.dealscraper.data.models.StoreMetadataModel

data class StoreMetadataDto(val storeId: Int, val name: String, val url: String){
    fun toModel() = StoreMetadataModel(id = storeId, name = name, url = url)
}
