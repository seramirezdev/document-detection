package com.seramirezdev.document_detection_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.seramirezdev.document_detection_app.databinding.ActivityMainBinding
import com.seramirezdev.lib.CameraTest

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.number.text = CameraTest().getNumber().toString()
    }
}