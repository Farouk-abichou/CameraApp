package com.example.cameraapp.feature.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cameraapp.feature.preview.PreviewImageSection
import com.example.cameraapp.feature.preview.PreviewUiState
import com.example.cameraapp.feature.preview.PreviewVideoSection
import org.koin.androidx.compose.get


@Composable
fun EditScreen(
    viewModel: EditViewModel = get(),
    onBackPressed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    when (val result: PreviewUiState = uiState) {
        PreviewUiState.Initial -> {}
        PreviewUiState.Empty -> {}
        is PreviewUiState.Ready -> {
            when {
                result.isVideo -> PreviewVideoSection(result.file)
                else -> PreviewImageSection(result.file)
            }
        }

        PreviewUiState.Deleted -> LaunchedEffect(Unit) {
            onBackPressed()
        }
    }
}

