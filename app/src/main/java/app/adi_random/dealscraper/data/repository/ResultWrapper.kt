package app.adi_random.dealscraper.data.repository

sealed class ResultWrapper<out T> {
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Error(val msg: String) : ResultWrapper<Nothing>()
    data class Loading(val isLoading: Boolean) : ResultWrapper<Nothing>()
}



