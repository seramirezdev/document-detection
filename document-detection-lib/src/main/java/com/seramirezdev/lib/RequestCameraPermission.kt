package com.seramirezdev.lib

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.seramirezdev.lib.extensions.showToast

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
            activity.showToast(R.string.camera_permissions_error)
            activity.finish()
        }
    }

    private fun checkCameraPermission() =
        ContextCompat.checkSelfPermission(
            activity,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}