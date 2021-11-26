package com.github.ai.fprovider.client.extension

import android.net.Uri
import com.github.ai.fprovider.client.data.entity.FilePath

fun FilePath.toUri(): Uri {
    return Uri.parse("content://$authority/$accessToken$path")
}

fun FilePath.toFilePath(accessToken: String): FilePath {
    return when {
        path == "/*" -> FilePath(
            authority = authority,
            path = "/",
            accessToken = accessToken
        )
        path.endsWith("/*") -> {
            val lastSlashIdx = path.lastIndexOf("/")
            FilePath(
                authority = authority,
                path = path.substring(0, lastSlashIdx),
                accessToken = accessToken
            )
        }
        else -> this
    }
}