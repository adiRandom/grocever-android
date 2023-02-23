package app.adi_random.dealscraper.data.repository

import app.adi_random.dealscraper.services.api.StoreApi
import kotlinx.coroutines.flow.flow

class StoreRepository(private val api:StoreApi) {
    fun getStore() = flow{
        emit(ResultWrapper.Loading(true))
        val apiResponse = api.getStores()
        if(apiResponse.isSuccessful){
            val stores = apiResponse.body
            emit(ResultWrapper.Success(stores.map { it.toModel() }))
        }else{
            emit(ResultWrapper.Error(apiResponse.err))
        }
        emit(ResultWrapper.Loading(false))
    }
}