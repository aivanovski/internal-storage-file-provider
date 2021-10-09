package com.github.ai.fprovider.demo.extension

import android.net.Uri
import com.github.ai.fprovider.demo.data.entity.FileEntity
import com.github.ai.fprovider.demo.data.entity.FilePath

fun FileEntity.toPath(): FilePath {
    val uri = Uri.parse(path)

    return FilePath(
        authority = uri.authority ?: throw IllegalStateException(),
        path = if (isDirectory) {
            "${uri.path}/*"
        } else {
            uri.path ?: throw IllegalStateException()
        }
    )
}