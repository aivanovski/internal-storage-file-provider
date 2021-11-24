package com.github.ai.fprovider.demo.presentation.file_list.cells.model

import androidx.annotation.DrawableRes

data class FileCellModel(
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val iconResId: Int,
    val onClick: ((id: String) -> Unit)? = null,
    val onLongClick: ((id: String) -> Unit)? = null
)