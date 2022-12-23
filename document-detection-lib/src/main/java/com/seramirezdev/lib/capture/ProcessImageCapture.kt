package com.seramirezdev.lib.capture

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.PointF
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore.Images.Media
import android.provider.MediaStore.MediaColumns
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import com.seramirezdev.lib.R
import com.seramirezdev.lib.extensions.showToast
import com.seramirezdev.lib.extensions.toBitmap
import com.seramirezdev.lib.scanner.DocumentScanner.findDocumentCorners
import org.opencv.android.Utils
import org.opencv.core.Mat
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

@ExperimentalGetImage
class ProcessImageCapture(
    private val context: Context,
    private val onImageSaved: (String) -> Unit
) : ImageCapture.OnImageCapturedCallback() {

    override fun onError(exc: ImageCaptureException) {
        context.showToast(R.string.failed_capture_photo)
    }

    override fun onCaptureSuccess(imageProxy: ImageProxy) {
        if (imageProxy.image == null) {
            context.showToast(R.string.failed_capture_photo)
            return
        }

        val mat = Mat()
        val bitmap = imageProxy.toBitmap() ?: return
        Utils.bitmapToMat(bitmap, mat)
        val corners = findDocumentCorners(mat.nativeObjAddr)

        if (corners.isEmpty()) context.showToast(R.string.document_no_detected)

        val sorted = sortCorners(corners)
        val startX = sorted.first().point.x
        val startY = sorted.first().point.y
        val endX = sorted.last().point.x - startX
        val endY = sorted.last().point.y - startY

        val croppedBitmap = Bitmap.createBitmap(bitmap, startX, startY, endX, endY)
        val fileName = saveBitmapToStorage(croppedBitmap)
        onImageSaved(fileName)
        imageProxy.close()
    }

    private fun sortCorners(corners: Array<IntArray>): List<PointDistance> {
        val sorted = mutableListOf<PointDistance>()
        val point = PointF(0F, 0F)

        corners.forEach {
            val dist = (point.x - it[0]).pow(2) + (point.y - it[1]).pow(2)
            sorted.add(PointDistance(sqrt(dist), Point(it[0], it[1])))
        }
        sorted.sortBy { it.dist }

        return sorted
    }

    private fun saveBitmapToStorage(bitmap: Bitmap): String {
        val fileName = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(currentTimeMillis())
        var fos: OutputStream? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaColumns.MIME_TYPE, "image/jpeg")
                        put(MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                    val imageUri: Uri? = resolver.insert(Media.EXTERNAL_CONTENT_URI, contentValues)
                    fos = imageUri?.let { resolver.openOutputStream(it) }
                }
            } else {
                val imagesDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, fileName)
                fos = FileOutputStream(image)
            }
            fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        } catch (ex: Exception) {
            context.showToast(R.string.error_saving_photo)
        }

        return fileName
    }

    private data class PointDistance(val dist: Float, val point: Point)

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}