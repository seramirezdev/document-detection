package com.seramirezdev.lib.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

fun AppCompatActivity.getExecutor(): Executor {
    return ContextCompat.getMainExecutor(this)
}