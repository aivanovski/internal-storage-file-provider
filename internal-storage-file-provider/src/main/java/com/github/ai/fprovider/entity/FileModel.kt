package com.github.ai.fprovider.entity

import com.github.ai.fprovider.utils.FileUtils.getExtensionFromName

internal data class FileModel(
    val path: String,
    val name: String,
    val size: Long,
    val isDirectory: Boolean
) {
    val extension: String? = if (!isDirectory) {
        getExtensionFromName(name)
    } else {
        null
    }
}