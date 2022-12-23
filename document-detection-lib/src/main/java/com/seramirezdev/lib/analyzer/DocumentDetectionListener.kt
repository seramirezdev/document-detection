package com.seramirezdev.lib.analyzer

fun interface DocumentDetectionListener {

    sealed interface State {
        object Scanning : State
        object DocumentDetected : State
    }

    fun onStateChanged(state: State)
}