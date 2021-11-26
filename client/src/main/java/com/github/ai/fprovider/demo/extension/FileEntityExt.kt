package com.github.ai.fprovider.demo.extension

import android.net.Uri
import com.github.ai.fprovider.demo.data.entity.FileEntity
import com.github.ai.fprovider.demo.data.entity.FilePath
import com.github.ai.fprovider.demo.utils.StringUtils.EMPTY

fun FileEntity.toFilePath(accessToken: String): FilePath {
    val uri = Uri.parse(path)

    return FilePath(
        authority = uri.authority ?: EMPTY,
        path = if (isDirectory) {
            "${uri.path}/*"
        } else {
            uri.path ?: EMPTY
        },
        accessToken = accessToken
    )
}

fun FileEntity.isHiddenFile(): Boolean {
    return name.startsWith(".")
}

fun FileEntity.getCleanPath(): String {
    val uri = Uri.parse(path) ?: return EMPTY
    return uri.path ?: EMPTY
}