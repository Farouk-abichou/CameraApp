package com.example.cameraapp.feature.camera.model

import androidx.annotation.StringRes
import com.example.cameraapp.composer.state.capture.mode.CaptureMode
import com.example.cameraapp.R

enum class CameraOption(@StringRes val titleRes: Int) {
    Photo(R.string.photo),
    Video(R.string.video),
    QRCode(R.string.qr_code);

    fun toCaptureMode(): CaptureMode = when(this) {
        QRCode, Photo -> CaptureMode.Image
        Video -> CaptureMode.Video
    }
}
