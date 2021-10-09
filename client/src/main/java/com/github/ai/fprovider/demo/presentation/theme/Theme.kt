package com.github.ai.fprovider.demo.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = AppColors,
        typography = AppTypography,
        content = content
    )
}