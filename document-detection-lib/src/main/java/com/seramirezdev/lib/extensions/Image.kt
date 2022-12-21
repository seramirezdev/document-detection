package com.seramirezdev.lib.extensions

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.seramirezdev.lib.converter.YuvToRgbConverter

@ExperimentalGetImage
fun ImageProxy.toBitmap(converter: YuvToRgbConverter): Bitmap {
    val bitmap = Bitmap.createBitmap(image!!.width, image!!.height, Bitmap.Config.ARGB_8888)
    converter.yuvToRgb(image!!, bitmap)
    return bitmap
}

fun Bitmap.rotate(degrees: Int): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}