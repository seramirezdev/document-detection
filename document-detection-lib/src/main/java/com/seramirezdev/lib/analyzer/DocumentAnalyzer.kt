package com.seramirezdev.lib.analyzer

import android.content.Context
import android.graphics.PointF
import android.util.Size
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.seramirezdev.lib.converter.YuvToRgbConverter
import com.seramirezdev.lib.extensions.rotate
import com.seramirezdev.lib.extensions.toBitmap
import com.seramirezdev.lib.models.Line
import com.seramirezdev.lib.views.OverlayView
import org.opencv.android.Utils
import org.opencv.core.Mat

@ExperimentalGetImage
class DocumentAnalyzer(
    context: Context, private val overlayView: OverlayView
) : ImageAnalysis.Analyzer {

    private val converter = YuvToRgbConverter(context)

    override fun analyze(imageProxy: ImageProxy) {
        if (imageProxy.image == null) {
            imageProxy.close()
            return
        }
        overlayView.setPreviewSize(Size(imageProxy.width, imageProxy.height))

        val bitmap = imageProxy.toBitmap(converter)
        val frame = Mat()
        val rotation = imageProxy.imageInfo.rotationDegrees

        Utils.bitmapToMat(bitmap.rotate(rotation), frame)

        val corners = scanFrame(frame.nativeObjAddr)
        overlayView.setLines(getLines(corners))
        imageProxy.close()
    }

    private external fun scanFrame(frameObjAddress: Long): Array<IntArray>

    private fun getLines(corners: Array<IntArray>): List<Line> {
        val size = corners.size
        return corners.mapIndexed { i, corner ->
            if (i == 0) {
                Line(
                    startPoint = corners[size - 1].getPoint(),
                    endPoint = corner.getPoint()
                )
            } else {
                Line(
                    startPoint = corners[i - 1].getPoint(),
                    endPoint = corner.getPoint()
                )
            }
        }
    }

    private fun IntArray.getPoint() = PointF(this[0].toFloat(), this[1].toFloat())

    companion object {
        init {
            System.loadLibrary("opencv_java3")
        }
    }
}