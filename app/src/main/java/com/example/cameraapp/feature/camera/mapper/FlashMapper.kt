package com.example.cameraapp.feature.camera.mapper

import com.example.cameraapp.composer.state.capture.mode.FlashMode
import com.example.cameraapp.feature.camera.model.Flash

fun Flash.toFlashMode() = when (this) {
    Flash.Auto -> FlashMode.Auto
    Flash.On -> FlashMode.On
    Flash.Off, Flash.Always -> FlashMode.Off
}

fun FlashMode.toFlash(isTorchEnabled: Boolean) = when (this) {
    FlashMode.On -> Flash.On
    FlashMode.Auto -> Flash.Auto
    FlashMode.Off -> Flash.Off
}.takeIf { !isTorchEnabled } ?: Flash.Always
