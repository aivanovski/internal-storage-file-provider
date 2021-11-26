package com.github.ai.fprovider.client.presentation.file_list.model

import android.net.Uri

data class OpenFileModel(
    val uri: Uri,
    val mimeType: String
)