package com.seramirezdev.document_detection_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.seramirezdev.document_detection_app.databinding.ActivityMainBinding
import com.seramirezdev.lib.CameraActivity

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == CameraActivity.RESULT_OK) {
                result.data?.let { getImageUri(it) }
            }
        }

        binding.scanBtn.setOnClickListener {
            launcher.launch(Intent(this@MainActivity, CameraActivity::class.java))
        }
    }

    private fun getImageUri(data: Intent) {
        val uri = data.extras?.getString(CameraActivity.IMAGE_URI_KEY)
        Log.d("SERGIO", "Uri $uri")
    }
}