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
        val f = File(ctx.getCacheDir(), "temp");
        f.createNewFile();

        val invoiceBitmap = this.getBitmapFromUri(uri, ctx)
        var bos = ByteArrayOutputStream()
        invoiceBitmap?.compress(
            Bitmap.CompressFormat.JPEG,
            100 /*ignored for PNG*/,
            bos
        )
        var bitmapdata = bos.toByteArray()

        var quality = 99
        while (bitmapdata.size > 4500000) {
            bos = ByteArrayOutputStream()
            invoiceBitmap?.compress(Bitmap.CompressFormat.JPEG, quality, bos)
            bitmapdata = bos.toByteArray()
            quality--
        }

        // write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()

        return f
    }
}