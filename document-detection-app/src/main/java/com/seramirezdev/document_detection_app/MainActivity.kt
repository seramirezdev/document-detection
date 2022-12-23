package com.seramirezdev.document_detection_app

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.seramirezdev.document_detection_app.databinding.ActivityMainBinding
import com.seramirezdev.lib.CameraActivity
import com.seramirezdev.lib.extensions.rotate
import java.io.File

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == CameraActivity.RESULT_OK) {
                    result.data?.let { getImageUri(it) }
                }
            }

        binding.scanBtn.setOnClickListener {
            launcher.launch(Intent(this, CameraActivity::class.java))
        }
    }

    private fun getImageUri(data: Intent) {
        val imagePath = data.extras?.getString(CameraActivity.IMAGE_URI_KEY) ?: return
        val file = File(getExternalStoragePublicDirectory(DIRECTORY_PICTURES), imagePath.plus(".jpg"))
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            bitmap.rotate(90)
            binding.documentScannedImg.isVisible = true
            binding.startScanningText.isVisible = false
            binding.documentScannedImg.setImageBitmap(bitmap)
        }
    }
}