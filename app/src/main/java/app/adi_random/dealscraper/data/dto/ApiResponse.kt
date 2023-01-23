package app.adi_random.dealscraper.data.dto

data class ApiResponse<T>(val statusCode: Int, val body: T, val error: String){
    val isSuccessful: Boolean
        get() = statusCode in 200..299 && error.isEmpty()
}
