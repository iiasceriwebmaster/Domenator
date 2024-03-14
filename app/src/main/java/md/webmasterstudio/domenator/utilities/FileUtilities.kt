package md.webmasterstudio.domenator.utilities
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Base64
import androidx.core.content.FileProvider
import com.daimajia.androidanimations.library.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.platform.android.AndroidLogHandler.flush
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID

object FileUtilities {
    suspend fun saveImageToDevice(context: Context, imageUri: Uri, isDocument: Boolean): String {
        val filename = "${UUID.randomUUID()}.jpg"
        val contentResolver = context.contentResolver
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
        val outputStream = contentResolver.openOutputStream(getSaveFilePath(context, filename, isDocument))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream!!)
        withContext(Dispatchers.IO) {
            outputStream.flush()
            outputStream?.close()
        }
        return filename
    }

    private fun getSaveFilePath(context: Context, filename: String, isDocument: Boolean): Uri {
        val directory = if (isDocument) {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        } else {
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        }
        directory?.mkdirs()
        val file = File(directory, filename)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val scopedUri = FileProvider.getUriForFile(
                context,
                "md.webmasterstudio.domenator" + ".fileprovider",
                file
            )
            scopedUri
        } else {
            Uri.fromFile(file)
        }
    }


    fun uriToBase64(contentResolver: ContentResolver, uri: Uri): String {
        // Get the bitmap from the URI
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))

        // Convert the bitmap to byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        // Encode the byte array to Base64
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun base64ListToUriList(contentResolver: ContentResolver, base64List: List<String>): List<Uri> {
        return base64List.map { base64ToUri(contentResolver, it) }
    }

    // Function to convert Base64 to URI
    fun base64ToUri(contentResolver: ContentResolver, base64String: String): Uri {
        // Decode Base64 string to byte array
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)

        // Convert byte array to input stream
        val inputStream = ByteArrayInputStream(decodedBytes)

        // Create bitmap from input stream
        val bitmap = BitmapFactory.decodeStream(inputStream)

        // Convert bitmap to URI
        val uri = bitmapToUri(contentResolver, bitmap)

        return uri
    }

    fun bitmapToUri(contentResolver: ContentResolver, bitmap: Bitmap): Uri {
        // Convert bitmap to URI
        // This is just an example implementation, replace with your logic to create URI from bitmap
        // You may need to save the bitmap to a temporary file and return its URI
        // Here, we return a placeholder URI
        val uri = Uri.parse("content://com.example.placeholder/image")

        return uri
    }
}