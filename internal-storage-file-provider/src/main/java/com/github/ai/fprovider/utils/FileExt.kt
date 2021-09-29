package com.github.ai.fprovider.utils

import com.github.ai.fprovider.entity.FileModel
import java.io.File

internal fun List<File>.toModels(
    trimPathPrefix: String? = null
): List<FileModel> {
    return map { it.toModel(trimPathPrefix) }
}

internal fun File.toModel(trimPathPrefix: String? = null): FileModel {
    return FileModel(
        path = if (trimPathPrefix != null && path.startsWith(trimPathPrefix)) {
            path.substringAfter(trimPathPrefix)
        } else {
            path
        },
        name = name,
        size = length(),
        isDirectory = isDirectory
    )
}
