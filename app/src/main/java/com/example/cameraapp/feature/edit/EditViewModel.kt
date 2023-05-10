package com.example.cameraapp.feature.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameraapp.extensions.isVideo
import com.example.cameraapp.feature.preview.PreviewUiState
import com.example.cameraapp.router.Args
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File

class EditViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<PreviewUiState>(PreviewUiState.Initial)
    val uiState: StateFlow<PreviewUiState> = _uiState

    init {
        viewModelScope.launch {
            flow {
                val path =
                    savedStateHandle.get<String?>(Args.Path) ?: return@flow emit(PreviewUiState.Empty)
                val file = File(Uri.decode(path))
                emit(PreviewUiState.Ready(file, file.isVideo))
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}