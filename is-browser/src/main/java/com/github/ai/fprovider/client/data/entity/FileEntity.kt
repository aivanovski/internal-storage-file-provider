package com.github.ai.fprovider.client.data.entity

data class FileEntity(
    val path: String,
    val name: String,
    val size: Long?,
    val mimeType: String?,
    val isDirectory: Boolean
)