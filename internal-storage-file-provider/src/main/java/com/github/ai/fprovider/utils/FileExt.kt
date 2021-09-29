package com.github.ai.fprovider.utils

import com.github.ai.fprovider.entity.FileModel
import java.io.File

internal fun List<File>.toModels(): List<FileModel> {
    return map { it.toModel() }
}

internal fun File.toModel(): FileModel {
    return FileModel(
        path = path,
        name = name,
        size = length(),
        isDirectory = isDirectory
    )
}
