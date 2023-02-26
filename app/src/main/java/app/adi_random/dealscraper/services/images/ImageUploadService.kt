package app.adi_random.dealscraper.services.images

import android.content.Context
import android.media.Image
import android.net.Uri
import androidx.lifecycle.viewModelScope
import androidx.work.*
import app.adi_random.dealscraper.data.repository.GalleryRepository
import app.adi_random.dealscraper.data.repository.OcrProductRepository
import app.adi_random.dealscraper.data.repository.ResultWrapper
import app.adi_random.dealscraper.usecase.ImageUseCase
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.getKoin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class ImageUploadService(
    private val ctx: Context,
    private val galleryRepository: GalleryRepository,
    private val imageDetectionService: ImageDetectionService,
    private val ocrProductRepository: OcrProductRepository
) {

    class Worker(
        ctx: Context,
        workerParameters: WorkerParameters,
    ) : KoinComponent, CoroutineWorker(ctx, workerParameters) {
        override suspend fun doWork(): Result {
            return withContext(Dispatchers.IO) {
                val uploadService by inject<ImageUploadService>()
                uploadService.findImagesAndUpload()
                Result.success()
            }
        }
    }

    private suspend fun getAllImages(): List<Uri> {
        return galleryRepository.getUnprocessedImagesUris().filter() { uri ->
            ImageUseCase.getBitmapFromUri(uri, ctx)?.let { bitmap ->
                val isImageOfInterest =
                    imageDetectionService.isImageOfInterest(bitmap, ctx)

                isImageOfInterest
            } ?: false
        }
    }

    fun uploadImage(uri: Uri?, context: Context): Flow<ResultWrapper<Unit>> {
        return ocrProductRepository.uploadImage(uri, context)
    }

    private suspend fun uploadImageAndGetResult(uri: Uri?, context: Context): ResultWrapper<Unit> {
        //TODO: Uncomment in production
//        return ocrProductRepository.uploadImage(uri, context)
//            .first { it is ResultWrapper.Success || it is ResultWrapper.Error }
        return ResultWrapper.Success(Unit)
    }

    private suspend fun retryUploads() {
        withContext(Dispatchers.IO) {
            val retryUploads = galleryRepository.getRetryUploads()
            retryUploads.forEach { uri ->
                val result = uploadImageAndGetResult(uri, ctx)
                if (result is ResultWrapper.Success) {
                    galleryRepository.removeRetryUpload(uri)
                }
            }
        }
    }

    suspend fun findImagesAndUpload() {
        getAllImages().forEach { uri ->
            val result = uploadImageAndGetResult(uri, ctx)

            if (result is ResultWrapper.Error) {
                galleryRepository.saveImageForRetry(uri)
            }
        }

        retryUploads()
    }

    suspend fun startService() {
        // TODO: This if always passes. The return line in shouldStartService is never reached...but the if passes
        if (shouldStartService().not()) {
            return
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val request =
            PeriodicWorkRequestBuilder<Worker>(12, TimeUnit.HOURS).setConstraints(constraints)
                .build()

        WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
            UPLOAD_JOB_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )

        // Also launch the job right now
        findImagesAndUpload()
    }

    private suspend fun shouldStartService():Boolean {
        val serviceInfo = WorkManager.getInstance(ctx).getWorkInfosByTag(UPLOAD_JOB_NAME).await()
        return serviceInfo.isEmpty()
    }

    companion object {
        private const val UPLOAD_JOB_NAME = "ImageUploadService"
    }
}