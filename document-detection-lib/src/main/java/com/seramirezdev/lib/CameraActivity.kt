package com.seramirezdev.lib

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import com.seramirezdev.lib.analyzer.DocumentDetector
import com.seramirezdev.lib.databinding.ActivityCameraBinding
import com.seramirezdev.lib.extensions.getExecutor
import com.seramirezdev.lib.extensions.showToast
import com.seramirezdev.lib.models.Line

internal class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    @ExperimentalGetImage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RequestCameraPermission(this, onSuccess = ::startCamera)
    }

    @ExperimentalGetImage
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = setupCameraPreview()
            val imageCapture = setupImageCapture()
            val imageAnalysis = setupImageAnalysis()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalysis
                )
            } catch (exc: Exception) {
                showToast(R.string.failed_access_camera)
                finish()
            }
        }, getExecutor())
    }

    private fun setupCameraPreview(): Preview {
        return Preview.Builder().build().apply {
            setSurfaceProvider(binding.viewFinder.surfaceProvider)
        }
    }

    private fun setupImageCapture(): ImageCapture {
        return ImageCapture.Builder().build().apply {
            binding.takePhotoBtn.setOnClickListener { takePhoto(this) }
        }
    }

    @ExperimentalGetImage
    private fun setupImageAnalysis(): ImageAnalysis {
        return ImageAnalysis.Builder().build().apply {
            setAnalyzer(getExecutor(), DocumentDetector(this@CameraActivity, ::onDocumentDetected))
        }
    }

    private fun onDocumentDetected(lines: List<Line>) {
        binding.documentDetectedOverlay.drawLines(lines)
    }

    private fun takePhoto(imageCapture: ImageCapture) {
        imageCapture.takePicture(
            getExecutor(), object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onCaptureSuccess(image: ImageProxy) {
                    image.close()
                }
            }
        )
    }

    companion object {
        private val TAG = CameraActivity::class.simpleName

        init {
            System.loadLibrary("native-lib")
        }
    }
}

fun showCamera(context: Context) {
    with(context) {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }
}