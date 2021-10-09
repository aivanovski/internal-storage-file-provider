package com.github.ai.fprovider.demo.presentation.file_list.cells

import androidx.annotation.DrawableRes

data class FileCellModel(
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val iconResId: Int,
    val onClick: ((id: String) -> Unit)? = null
)