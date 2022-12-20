package com.seramirezdev.document_detection_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.seramirezdev.document_detection_app.databinding.ActivityMainBinding
import com.seramirezdev.lib.showCamera

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.scanBtn.setOnClickListener { showCamera(this) }
    }
}