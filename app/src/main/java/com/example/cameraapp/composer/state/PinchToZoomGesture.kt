package com.example.cameraapp.composer.state

import android.view.ScaleGestureDetector

internal class PinchToZoomGesture(
    private val onZoomChanged: (Float) -> Unit
) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
    override fun onScale(detector: ScaleGestureDetector): Boolean {
        onZoomChanged(detector.scaleFactor)
        return true
    }

}