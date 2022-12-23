package com.seramirezdev.lib

import android.content.Intent
import android.graphics.ImageFormat
import android.os.Bundle
import android.util.Size
import android.view.MotionEvent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.TOP
import com.seramirezdev.lib.CameraActivity.Companion.IMAGE_URI_KEY
import com.seramirezdev.lib.CameraActivity.Companion.RESULT_OK
import com.seramirezdev.lib.analyzer.DocumentAnalyzer
import com.seramirezdev.lib.analyzer.DocumentDetectionListener.State
import com.seramirezdev.lib.capture.ProcessImageCapture
import com.seramirezdev.lib.databinding.ActivityCameraBinding
import com.seramirezdev.lib.extensions.getExecutor
import com.seramirezdev.lib.extensions.showToast
import com.seramirezdev.lib.views.OverlayView

@ExperimentalGetImage
internal class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    private lateinit var overlayView: OverlayView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        RequestCameraPermission(this, onSuccess = ::startCamera)
        setOverlay()
    }

    private fun setOverlay() {
        overlayView = OverlayView(this)
        addContentView(overlayView, LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
        ConstraintSet().apply {
            clone(binding.root)
            connect(overlayView.id, TOP, binding.viewFinder.id, TOP)
            connect(overlayView.id, BOTTOM, binding.viewFinder.id, BOTTOM)
            connect(overlayView.id, START, binding.viewFinder.id, START)
            connect(overlayView.id, END, binding.viewFinder.id, END)
            binding.root.setConstraintSet(this)
        }
    }

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
                val control = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalysis
                )
                setupTapToFocus(control)
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
        return ImageCapture.Builder()
            .setBufferFormat(ImageFormat.YUV_420_888)
            .setTargetResolution(Size(1080, 1920))
            .build().apply {
                binding.takePhotoBtn.setOnClickListener { takePhoto(this) }
            }
    }

    private fun setupImageAnalysis(): ImageAnalysis {
        val documentAnalyzer = DocumentAnalyzer(overlayView) { state ->
            binding.detectionStateTxt.text = if (state is State.Scanning) {
                getString(R.string.scanning)
            } else {
                getString(R.string.document_detected)
            }
        }
        return ImageAnalysis.Builder().build().apply {
            setAnalyzer(getExecutor(), documentAnalyzer)
        }
    }

    private fun takePhoto(imageCapture: ImageCapture) {
        imageCapture.takePicture(getExecutor(), ProcessImageCapture(this) { uri ->
            val intent = Intent().apply {
                putExtra(IMAGE_URI_KEY, uri)
            }
            setResult(RESULT_OK, intent)
            finish()
        })
    }

    private fun setupTapToFocus(camera: Camera) {
        binding.viewFinder.setOnTouchListener(View.OnTouchListener { v, motionEvent ->
            val eventConsumed = when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> true
                MotionEvent.ACTION_UP -> {
                    val factory = binding.viewFinder.meteringPointFactory
                    val point = factory.createPoint(motionEvent.x, motionEvent.y)
                    val action = FocusMeteringAction.Builder(point).build()
                    camera.cameraControl.startFocusAndMetering(action)
                    true
                }
                else -> false
            }
            v.performClick()
            return@OnTouchListener eventConsumed
        })
    }

    companion object {

        const val RESULT_OK = 1
        const val IMAGE_URI_KEY = "IMAGE_URI_KEY"

        init {
            System.loadLibrary("native-lib")
        }
    }
}

@ExperimentalGetImage
fun showCamera(activity: AppCompatActivity) {
    activity.registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.extras?.getString(IMAGE_URI_KEY)
        }
    }.apply {
        launch(Intent(activity, CameraActivity::class.java))
    }
}