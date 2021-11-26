package com.github.ai.fprovider.client.presentation.file_list.cells.model

import android.net.Uri
import androidx.annotation.DrawableRes

data class FileCellModel(
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val iconResId: Int,
    val imageUri: Uri? = null,
    val onClick: ((id: String) -> Unit)? = null,
    val onLongClick: ((id: String) -> Unit)? = null
)