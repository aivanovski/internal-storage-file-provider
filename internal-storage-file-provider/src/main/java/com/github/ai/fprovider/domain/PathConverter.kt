package com.github.ai.fprovider.domain

import android.net.Uri

internal class PathConverter {

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