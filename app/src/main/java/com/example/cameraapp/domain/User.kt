package com.example.cameraapp.domain

import androidx.compose.runtime.Immutable

@Immutable
data class User(
    val usePinchToZoom: Boolean,
    val useTapToFocus: Boolean,
    val useCamFront: Boolean,
)
