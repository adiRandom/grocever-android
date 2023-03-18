package app.adi_random.dealscraper.data.dto.product

import app.adi_random.dealscraper.data.models.ReportMissLinkModel

data class ReportMissLinkDto(
    val productId: Int,
    val ocrProductName: String
){
    fun toModel() = ReportMissLinkModel(productId, ocrProductName)
}
