package com.seramirezdev.lib.views

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Size
import android.view.View
import com.seramirezdev.lib.extensions.translate
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

    init {
        id = generateViewId()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
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

    fun setLines(lines: Array<Line>) {
        this.lines = lines
        postInvalidate()
    }

    private fun drawOverlay(canvas: Canvas) {
        widthScaleFactor = width.toFloat() / previewWidth
        heightScaleFactor = height.toFloat() / previewHeight
        lines.forEach { line ->
            with(line) {
                val startX = startPoint.x.translate(widthScaleFactor)
                val startY = startPoint.y.translate(heightScaleFactor)
                val endX = endPoint.x.translate(widthScaleFactor)
                val endY = endPoint.y.translate(heightScaleFactor)
                canvas.drawLine(startX, startY, endX, endY, paint)
            }
        }
    }
}