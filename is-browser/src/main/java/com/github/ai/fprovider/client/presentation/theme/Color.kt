package com.github.ai.fprovider.client.presentation.theme

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val primaryColor = Color(0xFF3F51B5)
val primaryDarkColor = Color(0xFF303F9F)
val accentColor = Color(0xFFFF4081)
val dividerColor = Color(0xFFB6B6B6)
val greyLight = Color(0xFFEEEEEE)
val primaryTextColor = Color(0xFF212121)
val secondaryTextColor = Color(0xFF757575)
val errorTextColor = Color(0xFFC00020)
val backgroundColor = Color.White

val AppColors = lightColors(
    primary = primaryColor,
    primaryVariant = primaryDarkColor,
    background = backgroundColor,
    error = errorTextColor,
    onSurface = backgroundColor
)