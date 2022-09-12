package app.adi_random.dealscraper.services

import android.content.Context
import android.graphics.Bitmap
import app.adi_random.dealscraper.constants.PathConstants
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.ImageClassifier.ImageClassifierOptions
import java.util.*


class ImageDetectionService {
    private fun detectImage(image: Bitmap, context: Context): String {
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(EXPECTED_IMAGE_SIZE, EXPECTED_IMAGE_SIZE, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0.5f, 0.5f))
            .build()


        val options = ImageClassifierOptions.builder()
            .setBaseOptions(BaseOptions.builder().useNnapi().build())
            .setMaxResults(1)
            .build()

        val imageClassifier = ImageClassifier.createFromFileAndOptions(
            context, PathConstants.IMAGE_RECOGNITION_MODEL_PATH, options
        )

        val tfImage = TensorImage(DataType.FLOAT32)
        tfImage.load(image)
        val processedImage = imageProcessor.process(tfImage)
        val results: List<Classifications> = imageClassifier.classify(processedImage)
        return results[0].categories[0].label
    }

    fun isImageOfInterest(image: Bitmap, context: Context): Boolean {
        val label = detectImage(image, context)
        return label.lowercase(Locale.getDefault()) == MENU_LABEL
    }

    companion object {
        const val EXPECTED_IMAGE_SIZE = 224
        const val MENU_LABEL = "menu"
    }
}