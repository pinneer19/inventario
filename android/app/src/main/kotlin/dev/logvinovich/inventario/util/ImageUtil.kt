package dev.logvinovich.inventario.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ImageUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun compressImage(
        contentUri: Uri,
        compressionThreshold: Long = 1024 * 1024
    ): ByteArray? {
        return withContext(Dispatchers.IO) {
            val mimeType = context.contentResolver.getType(contentUri)
            val inputBytes = context
                .contentResolver
                .openInputStream(contentUri)
                ?.use { inputStream ->
                    inputStream.readBytes()
                } ?: return@withContext null

            if (inputBytes.size <= compressionThreshold) {
                return@withContext inputBytes
            }

            ensureActive()

            withContext(Dispatchers.Default) {
                val bitmap = BitmapFactory.decodeByteArray(inputBytes, 0, inputBytes.size)
                    ?: return@withContext null

                ensureActive()

                val compressFormat = when (mimeType) {
                    "image/png" -> Bitmap.CompressFormat.PNG
                    "image/jpeg" -> Bitmap.CompressFormat.JPEG
                    "image/webp" -> if (Build.VERSION.SDK_INT >= 30) {
                        Bitmap.CompressFormat.WEBP_LOSSLESS
                    } else Bitmap.CompressFormat.WEBP

                    else -> Bitmap.CompressFormat.JPEG
                }

                var outputBytes: ByteArray
                var quality = 95

                do {
                    ByteArrayOutputStream().use { outputStream ->
                        bitmap.compress(compressFormat, quality, outputStream)
                        outputBytes = outputStream.toByteArray()
                        quality -= 5
                    }
                } while (isActive &&
                    outputBytes.size > compressionThreshold &&
                    quality > 5 &&
                    compressFormat != Bitmap.CompressFormat.PNG
                )

                outputBytes
            }
        }
    }
}