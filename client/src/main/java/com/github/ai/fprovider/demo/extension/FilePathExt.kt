package com.github.ai.fprovider.demo.extension

import android.net.Uri
import com.github.ai.fprovider.demo.data.entity.FilePath

fun FilePath.toUri(): Uri {
    return Uri.parse("content://$authority$path")
}

fun FilePath.toFilePath(): FilePath {
    return when {
        path == "/*" -> FilePath(
            authority = authority,
            path = "/"
        )
        path.endsWith("/*") -> {
            val lastSlashIdx = path.lastIndexOf("/")
            FilePath(
                authority = authority,
                path = path.substring(0, lastSlashIdx)
            )
        }
        else -> this
    }
}