package com.seramirezdev.lib.views

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Size
import android.view.View
import com.seramirezdev.lib.models.Line

class OverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var previewWidth: Int = 0
    private var widthScaleFactor = 1.0f
    private var previewHeight: Int = 0
    private var heightScaleFactor = 1.0f

    private var lines = emptyArray<Line>()
    private var orientation = Configuration.ORIENTATION_PORTRAIT

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        style = Paint.Style.STROKE
        strokeWidth = 5.0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawOverlay(canvas)
    }

    fun setPreviewSize(size: Size) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            previewWidth = size.width
            previewHeight = size.height
        } else {
            previewWidth = size.height
            previewHeight = size.width
        }
    }

    fun setLines(lines: List<Line>) {
        this.lines = lines.toTypedArray()
        postInvalidate()
    }

    private fun drawOverlay(canvas: Canvas) {
        widthScaleFactor = width.toFloat() / previewWidth
        heightScaleFactor = height.toFloat() / previewHeight
        lines.forEach { line ->
            with(line) {
                val startX = translateX(startPoint.x)
                val startY = translateY(startPoint.y)
                val endX = translateX(endPoint.x)
                val endY = translateY(endPoint.y)
                canvas.drawLine(startX, startY, endX, endY, paint)
            }
        }
    }

    private fun translateX(x: Float): Float = x * widthScaleFactor
    private fun translateY(y: Float): Float = y * heightScaleFactor
}