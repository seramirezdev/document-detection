package com.seramirezdev.lib

class CameraTest {

    external fun getNumber(): Int

    companion object {

        init {
            System.loadLibrary("native-lib")
        }
    }
}