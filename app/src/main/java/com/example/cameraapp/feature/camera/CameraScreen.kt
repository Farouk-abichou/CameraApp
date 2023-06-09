package com.example.cameraapp.feature.camera

import android.widget.Toast
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.cloudy.Cloudy
import com.example.cameraapp.composer.CameraPreview
import com.example.cameraapp.composer.state.CamSelector
import com.example.cameraapp.composer.state.CameraState
import com.example.cameraapp.composer.state.rememberCamSelector
import com.example.cameraapp.composer.state.rememberCameraState
import com.example.cameraapp.composer.state.rememberFlashMode
import com.example.cameraapp.composer.state.rememberImageAnalyzer
import com.example.cameraapp.composer.state.rememberTorch
import com.example.cameraapp.extensions.noClickable
import com.example.cameraapp.feature.camera.components.ActionBox
import com.example.cameraapp.feature.camera.components.BlinkPictureBox
import com.example.cameraapp.feature.camera.components.SettingsBox
import com.example.cameraapp.feature.camera.mapper.toFlash
import com.example.cameraapp.feature.camera.mapper.toFlashMode
import com.example.cameraapp.feature.camera.model.CameraOption
import com.example.cameraapp.feature.camera.model.Flash
import org.koin.androidx.compose.get
import java.io.File

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = get(),
    onGalleryClick: () -> Unit,
    onConfigurationClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (val result: CameraUiState = uiState) {
        is CameraUiState.Ready -> {
            val cameraState = rememberCameraState()
            CameraSection(
                cameraState = cameraState,
                useFrontCamera = result.user.useCamFront,
                usePinchToZoom = result.user.usePinchToZoom,
                useTapToFocus = result.user.useTapToFocus,
                lastPicture = result.lastPicture,
                qrCodeText = result.qrCodeText,
                onGalleryClick = onGalleryClick,
                onConfigurationClick = onConfigurationClick,
                onRecording = { viewModel.toggleRecording(cameraState) },
                onTakePicture = { viewModel.takePicture(cameraState) },
                onAnalyzeImage = viewModel::analyzeImage
            )

            val context = LocalContext.current
            LaunchedEffect(result.throwable) {
                if (result.throwable != null) {
                    Toast.makeText(context, result.throwable.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        CameraUiState.Initial -> Unit
    }
}

@Composable
fun CameraSection(
    cameraState: CameraState,
    useFrontCamera: Boolean,
    usePinchToZoom: Boolean,
    useTapToFocus: Boolean,
    qrCodeText: String?,
    lastPicture: File?,
    onTakePicture: () -> Unit,
    onRecording: () -> Unit,
    onGalleryClick: () -> Unit,
    onAnalyzeImage: (ImageProxy) -> Unit,
    onConfigurationClick: () -> Unit,
) {
    var flashMode by cameraState.rememberFlashMode()
    var camSelector by rememberCamSelector(if (useFrontCamera) CamSelector.Front else CamSelector.Back)
    var zoomRatio by rememberSaveable { mutableStateOf(cameraState.minZoom) }
    var zoomHasChanged by rememberSaveable { mutableStateOf(false) }
    val hasFlashUnit by rememberUpdatedState(cameraState.hasFlashUnit)
    var cameraOption by rememberSaveable { mutableStateOf(CameraOption.Photo) }
    val isRecording by rememberUpdatedState(cameraState.isRecording)
    var enableTorch by cameraState.rememberTorch(initialTorch = false)
    val imageAnalyzer = cameraState.rememberImageAnalyzer(analyze = onAnalyzeImage)

    CameraPreview(
        cameraState = cameraState,
        camSelector = camSelector,
        captureMode = cameraOption.toCaptureMode(),
        enableTorch = enableTorch,
        flashMode = flashMode,
        zoomRatio = zoomRatio,
        imageAnalyzer = imageAnalyzer.takeIf { cameraOption == CameraOption.QRCode },
        isPinchToZoomEnabled = usePinchToZoom,
        isFocusOnTapEnabled = useTapToFocus,
        onZoomRatioChanged = {
            zoomHasChanged = true
            zoomRatio = it
        },
        onSwitchToFront = { bitmap ->
            Cloudy(radius = 20) { Image(bitmap.asImageBitmap(), contentDescription = null) }
        },
        onSwitchToBack = { bitmap ->
            Cloudy(radius = 20) { Image(bitmap.asImageBitmap(), contentDescription = null) }
        }
    ) {
        BlinkPictureBox(lastPicture, cameraOption == CameraOption.Video)
        CameraInnerContent(
            Modifier.fillMaxSize(),
            zoomHasChanged = zoomHasChanged,
            zoomRatio = zoomRatio,
            flashMode = flashMode.toFlash(enableTorch),
            isRecording = isRecording,
            cameraOption = cameraOption,
            hasFlashUnit = hasFlashUnit,
            qrCodeText = qrCodeText,
            onFlashModeChanged = { flash ->
                enableTorch = flash == Flash.Always
                flashMode = flash.toFlashMode()
            },
            onZoomFinish = { zoomHasChanged = false },
            lastPicture = lastPicture,
            onTakePicture = onTakePicture,
            onRecording = onRecording,
            onSwitchCamera = {
                if (cameraState.isStreaming) {
                    camSelector = camSelector.inverse
                }
            },
            onCameraOptionChanged = { cameraOption = it },
            onGalleryClick = onGalleryClick,
            onConfigurationClick = onConfigurationClick
        )
    }
}

@Composable
fun CameraInnerContent(
    modifier: Modifier = Modifier,
    zoomHasChanged: Boolean,
    zoomRatio: Float,
    flashMode: Flash,
    isRecording: Boolean,
    cameraOption: CameraOption,
    hasFlashUnit: Boolean,
    qrCodeText: String?,
    lastPicture: File?,
    onGalleryClick: () -> Unit,
    onFlashModeChanged: (Flash) -> Unit,
    onZoomFinish: () -> Unit,
    onRecording: () -> Unit,
    onTakePicture: () -> Unit,
    onConfigurationClick: () -> Unit,
    onSwitchCamera: () -> Unit,
    onCameraOptionChanged: (CameraOption) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        SettingsBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp, start = 24.dp, end = 24.dp),
            flashMode = flashMode,
            zoomRatio = zoomRatio,
            isVideo = cameraOption == CameraOption.Video,
            hasFlashUnit = hasFlashUnit,
            zoomHasChanged = zoomHasChanged,
            isRecording = isRecording,
            onFlashModeChanged = onFlashModeChanged,
            onConfigurationClick = onConfigurationClick,
            onZoomFinish = onZoomFinish,
        )
        ActionBox(
            modifier = Modifier
                .fillMaxWidth()
                .noClickable()
                .padding(bottom = 32.dp, top = 16.dp),
            lastPicture = lastPicture,
            onGalleryClick = onGalleryClick,
            cameraOption = cameraOption,
            qrCodeText = qrCodeText,
            onTakePicture = onTakePicture,
            isRecording = isRecording,
            onRecording = onRecording,
            onSwitchCamera = onSwitchCamera,
            onCameraOptionChanged = onCameraOptionChanged,
        )
    }
}
