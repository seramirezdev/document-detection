package com.seramirezdev.lib

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

internal class RequestCameraPermission(
    private val activity: AppCompatActivity,
    private val onSuccess: () -> Unit
) {

    init {
        if (checkCameraPermission()) {
            onSuccess()
        } else {
            activity.registerForActivityResult(
                RequestPermission(), ::onRequestPermissionResult
            ).apply {
                launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun onRequestPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            onSuccess()
        } else {
            Toast.makeText(
                activity, activity.getString(R.string.camera_permissions_error), Toast.LENGTH_SHORT
            ).show()
            activity.finish()
        }
    }

    private fun checkCameraPermission() =
        ContextCompat.checkSelfPermission(activity, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}