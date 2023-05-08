package com.example.cameraapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

@Composable
fun CamposerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = colorResource(id = R.color.primary),
            background = colorResource(id = R.color.light_gray),
        ),
        typography = Typography( TextStyle.Default ),
        content = content
    )
}
