package dev.logvinovich.inventario.util

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

fun String.generateBarCode(): Bitmap? {
    val width = 600
    val height = 180
    val bitmap = createBitmap(width, height)
    val codeWriter = MultiFormatWriter()
    return try {
        val bitMatrix = codeWriter.encode(
            this,
            BarcodeFormat.CODE_128,
            width,
            height
        )
        for (x in 0 until width) {
            for (y in 0 until height) {
                val color = if (bitMatrix[x, y]) Color.BLACK else Color.TRANSPARENT
                bitmap[x, y] = color
            }
        }
        bitmap
    } catch (_: WriterException) {
        null
    }
}