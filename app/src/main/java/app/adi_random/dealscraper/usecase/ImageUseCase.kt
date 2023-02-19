package app.adi_random.dealscraper.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream

object ImageUseCase {
    fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
        return try {
            val parcelFileDescriptor: ParcelFileDescriptor? = context.contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            image
        } catch (e: Exception) {
            null
        }
    }


    fun getFileFromImageUri(uri:Uri, ctx:Context): File{
        val f = File(ctx.cacheDir, "temp");
        f.createNewFile();

        val invoiceBitmap = this.getBitmapFromUri(uri, ctx)
        val bos = ByteArrayOutputStream()
        invoiceBitmap?.compress(
            Bitmap.CompressFormat.JPEG,
            100 /*ignored for PNG*/,
            bos
        )
        val bitmapdata = bos.toByteArray()

        // write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()

        return f
    }
}