package com.seramirezdev.lib.analyzer

import com.seramirezdev.lib.models.Line

fun interface DocumentDetectorListener {
    fun onDocumentDetected(lines: List<Line>)
}