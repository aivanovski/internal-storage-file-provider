package com.github.ai.fprovider.demo.data.entity

data class FileEntity(
    val path: String,
    val name: String,
    val size: Long?,
    val isDirectory: Boolean
)