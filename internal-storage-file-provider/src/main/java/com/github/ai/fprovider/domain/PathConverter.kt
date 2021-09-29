package com.github.ai.fprovider.domain

import android.net.Uri
import com.github.ai.fprovider.entity.QueryType

internal class PathConverter {

    fun getQueryType(uri: Uri): QueryType? {
        val path = uri.path ?: return null

        return if (path.endsWith("/*")) {
            QueryType.DIRECTORY_LIST
        } else {
            QueryType.FILE_INFO
        }
    }

    fun getPath(uri: Uri): String? {
        val path = uri.path
        if (path.isNullOrEmpty()) {
            return null
        }

        return when {
            path.endsWith("/*") -> {
                if (path.length > 2) {
                    path.substringBeforeLast("/*")
                } else {
                    "/"
                }
            }
            else -> path
        }
    }
}