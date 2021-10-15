package com.github.ai.fprovider.domain

import android.net.Uri
import com.github.ai.fprovider.entity.ParsedUri
import com.github.ai.fprovider.entity.QueryType

internal class UriParser {

    fun parse(uri: Uri): ParsedUri? {
        val queryType = getQueryType(uri) ?: return null

        val path = getPath(uri) ?: return null

        return ParsedUri(queryType, path)
    }

    private fun getQueryType(uri: Uri): QueryType? {
        val path = uri.path ?: return null

        return if (path.endsWith("/*")) {
            QueryType.DIRECTORY_LIST
        } else {
            QueryType.FILE_INFO
        }
    }

    private fun getPath(uri: Uri): String? {
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