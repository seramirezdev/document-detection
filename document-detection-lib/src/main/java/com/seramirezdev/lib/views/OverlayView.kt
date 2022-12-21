package com.seramirezdev.lib.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.seramirezdev.lib.models.Line

class OverlayView : View {

    constructor(c: Context?) : super(c) {
        init()
    }

    constructor(c: Context?, attr: AttributeSet?) : super(c, attr) {
        init()
    }

    constructor(c: Context?, attr: AttributeSet?, defStyle: Int) : super(c, attr, defStyle) {
        init()
    }

    private val lines = mutableListOf<Line>()

    private lateinit var paint: Paint

    private fun init() {
        paint = Paint().apply {
            color = Color.GREEN
            strokeWidth = 3.0f
            style = Paint.Style.STROKE
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        lines.forEach { line ->
            line.apply {
                canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint)
            }
        }
    }

    fun drawLines(lines: List<Line>) {
        this.lines.clear()
        this.lines.addAll(lines)
        invalidate()
    }
}