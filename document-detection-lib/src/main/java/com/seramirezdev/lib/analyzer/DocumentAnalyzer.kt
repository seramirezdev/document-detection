package com.seramirezdev.lib.analyzer

import android.graphics.PointF
import android.util.Size
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.seramirezdev.lib.analyzer.DocumentDetectionListener.*
import com.seramirezdev.lib.extensions.rotate
import com.seramirezdev.lib.extensions.toBitmap
import com.seramirezdev.lib.models.Line
import com.seramirezdev.lib.scanner.DocumentScanner.findDocumentCorners
import com.seramirezdev.lib.views.OverlayView
import org.opencv.android.Utils
import org.opencv.core.Mat

@ExperimentalGetImage
class DocumentAnalyzer(
    private val overlayView: OverlayView,
    private val listener: DocumentDetectionListener
) : ImageAnalysis.Analyzer {

    override fun analyze(imageProxy: ImageProxy) {
        if (imageProxy.image == null) {
            imageProxy.close()
            return
        }
        overlayView.setPreviewSize(Size(imageProxy.width, imageProxy.height))

        val rotation = imageProxy.imageInfo.rotationDegrees
        val bitmap = imageProxy.toBitmap()?.apply { rotate(rotation) } ?: return
        val frame = Mat()

        Utils.bitmapToMat(bitmap, frame)
        val corners = findDocumentCorners(frame.nativeObjAddr)
        val state = if (corners.isEmpty()) State.Scanning else State.DocumentDetected

        overlayView.setLines(getLines(corners))
        listener.onStateChanged(state)

        imageProxy.close()
    }

    private fun getLines(corners: Array<IntArray>): Array<Line> {
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
        }.toTypedArray()
    }

    private fun IntArray.getPoint() = PointF(this[0].toFloat(), this[1].toFloat())
}