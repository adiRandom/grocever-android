package app.adi_random.dealscraper.services.api.mock

import app.adi_random.dealscraper.data.dto.ApiResponse
import app.adi_random.dealscraper.data.dto.product.UserProductDto
import app.adi_random.dealscraper.data.dto.product.UserProductInstalmentDto
import app.adi_random.dealscraper.data.dto.product.UserProductListDto
import app.adi_random.dealscraper.data.models.ProductModel
import app.adi_random.dealscraper.services.api.ProductApi

class MockProductApi : ProductApi {
    override suspend fun getProducts(): ApiResponse<UserProductListDto> {
        return ApiResponse(
            200, UserProductListDto(
                listOf(
                    UserProductDto(
                        id = 1,
                        name = "Product 1",
                        ocrName = "Pr1",
                        purchaseInstalments = listOf(
                            UserProductInstalmentDto(
                                qty = 1f,
                                unitPrice = 10.0f
                            )
                        ),
                        bestPrice = 5.0f,
                        bestStoreUrl = "https://www.google.com",
                        bestStoreName = "Store 1",
                        bestStoreId = 1,
                        unitName = "kg"
                    ),
                    UserProductDto(
                        id = 2,
                        name = "Product 2",
                        ocrName = "Pr2",
                        purchaseInstalments = listOf(
                            UserProductInstalmentDto(
                                qty = 1f,
                                unitPrice = 20.0f
                            ),
                            UserProductInstalmentDto(
                                qty = 1f,
                                unitPrice = 20.0f
                            )
                        ),
                        bestPrice = 15.0f,
                        bestStoreUrl = "https://www.google.com",
                        bestStoreName = "Store 2",
                        bestStoreId = 2,
                        unitName = "kg"
                    ),
                    UserProductDto(
                        id = 3,
                        name = "Product 3",
                        ocrName = "Pr3",
                        purchaseInstalments = listOf(
                            UserProductInstalmentDto(
                                qty = 1f,
                                unitPrice = 30.0f
                            )
                        ),
                        bestPrice = 25.0f,
                        bestStoreUrl = "https://www.google.com",
                        bestStoreName = "Store 3",
                        bestStoreId = 3,
                        unitName = "kg"
                    )
                )
            ), ""
        )
    }
}