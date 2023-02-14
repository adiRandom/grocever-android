package app.adi_random.dealscraper.data.dto

import app.adi_random.dealscraper.data.repository.ResultWrapper

data class ApiResponse<T>(val statusCode: Int, val body: T, val err: String){
    val isSuccessful: Boolean
        get() = statusCode in 200..299 && err.isEmpty()

    fun toResultWrapper(): ResultWrapper<T> {
        return if (isSuccessful) {
            ResultWrapper.Success(body)
        } else {
            ResultWrapper.Error(err)
        }
    }
}
