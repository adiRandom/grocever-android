package app.adi_random.dealscraper.data.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import app.adi_random.dealscraper.services.ImageDetectionService
import app.adi_random.dealscraper.services.api.OcrProductApi
import app.adi_random.dealscraper.usecase.ImageUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class OcrProductRepository(private val ocrProductApi: OcrProductApi) {
    fun uploadImage(uri: Uri, context: Context): Flow<ResultWrapper<Unit>> = flow {
        emit(ResultWrapper.Loading(true))

        uri.path?.let { filePath ->
            val file = ImageUseCase.getFileFromImageUri(uri, context)
            val reqBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imageBodyPart = MultipartBody.Part.createFormData("image", file.name, reqBody)
            val response = ocrProductApi.uploadImage(imageBodyPart)

            if (response.isSuccessful) {
                emit(ResultWrapper.Success(Unit))
            } else {
                emit(ResultWrapper.Error(response.err))
            }
            emit(ResultWrapper.Loading(false))
        }
    }
}