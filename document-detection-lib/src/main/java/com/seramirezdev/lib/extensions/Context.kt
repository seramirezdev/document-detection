package com.seramirezdev.lib.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(@StringRes idRes: Int) {
    Toast.makeText(this, getText(idRes), Toast.LENGTH_LONG).show()
}