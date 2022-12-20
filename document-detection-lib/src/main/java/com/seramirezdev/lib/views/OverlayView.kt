package com.seramirezdev.lib.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class OverlayView(
    context: Context?, attr: AttributeSet?, defStyle: Int
) : View(context, attr, defStyle) {

    private var myRedPaint: Paint = Paint().apply {
        color = Color.RED
        strokeWidth = 3.0f
        style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(30f, 30f, 60f, 60f, myRedPaint)
    }
}