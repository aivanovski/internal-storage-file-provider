package com.github.ai.isfprovider.utils

import com.github.ai.isfprovider.entity.FileModel
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
        size = if (isDirectory) -1L else length(),
        isDirectory = isDirectory
    )
}
