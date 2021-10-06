package com.github.ai.fprovider.demo.model

import com.github.ai.fprovider.demo.ext.getFileNameFromPath

sealed class FileNode(
    open val path: String,
    open val files: List<FileNode>? = null
) {
    val name: String
        get() = path.getFileNameFromPath()
}

data class DirectoryNode(
    override val path: String,
    override val files: List<FileNode>
) : FileNode(path, files)

data class AssetFileNode(
    override val path: String,
) : FileNode(path, files = null)

data class StubFileNode(
    override val path: String,
    val content: String
) : FileNode(path, files = null)