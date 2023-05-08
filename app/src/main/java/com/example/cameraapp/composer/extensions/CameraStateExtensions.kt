package com.example.cameraapp.composer.extensions

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.camera.view.video.OutputFileOptions
import com.example.cameraapp.composer.state.CameraState
import com.example.cameraapp.composer.state.capture.result.ImageCaptureResult
import com.example.cameraapp.composer.state.capture.result.VideoCaptureResult
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


/**
 * Transform takePicture file to suspend function
 * */
public suspend fun CameraState.takePicture(file: File): Uri? = suspendCancellableCoroutine { cont ->
    with(cont) { takePicture(file, ::takePictureContinuation) }
}

/**
 * Transform takePicture content values to suspend function
 * */
public suspend fun CameraState.takePicture(
    contentValues: ContentValues,
    saveCollection: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
): Uri? = suspendCancellableCoroutine { cont ->
    with(cont) { takePicture(contentValues, saveCollection, ::takePictureContinuation) }
}

/**
 * Transform takePicture output files options to suspend function
 * */
public suspend fun CameraState.takePicture(
    outputFileOptions: ImageCapture.OutputFileOptions,
): Uri? = suspendCancellableCoroutine { cont ->
    with(cont) { takePicture(outputFileOptions, ::takePictureContinuation) }
}

/**
 * Transform toggle recording file to suspend function
 * */
public suspend fun CameraState.toggleRecording(file: File): Uri? = suspendCancellableCoroutine { cont ->
    with(cont) { toggleRecording(file, ::toggleRecordContinuation) }
}

/**
 * Transform toggle recording content values options to suspend function
 * */
public suspend fun CameraState.toggleRecording(
    contentValues: ContentValues,
    saveCollection: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
): Uri? = suspendCancellableCoroutine { cont ->
    with(cont) { toggleRecording(contentValues, saveCollection, ::toggleRecordContinuation) }
}

/**
 * Transform toggle recording output files options to suspend function
 * */
public suspend fun CameraState.toggleRecording(
    outputFileOptions: OutputFileOptions
): Uri? = suspendCancellableCoroutine { cont ->
    with(cont) { toggleRecording(outputFileOptions, ::toggleRecordContinuation) }
}

private fun Continuation<Uri?>.takePictureContinuation(result: ImageCaptureResult) {
    when (val res: ImageCaptureResult = result) {
        is ImageCaptureResult.Error -> resumeWithException(res.throwable)
        is ImageCaptureResult.Success -> resume(res.savedUri)
    }
}

private fun Continuation<Uri?>.toggleRecordContinuation(result: VideoCaptureResult) {
    when (val res: VideoCaptureResult = result) {
        is VideoCaptureResult.Error -> resumeWithException(res.throwable ?: Exception(res.message))
        is VideoCaptureResult.Success -> resume(res.savedUri)
    }
}
