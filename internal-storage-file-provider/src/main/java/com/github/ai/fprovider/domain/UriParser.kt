package com.github.ai.fprovider.domain

import android.net.Uri
import com.github.ai.fprovider.entity.ParsedUri
import com.github.ai.fprovider.entity.QueryType
import java.io.File

internal class UriParser {

    fun parse(uri: Uri): ParsedUri? {
        val queryType = getQueryType(uri) ?: return null

        val authToken = getAuthToken(uri) ?: return null

        val path = getPath(uri) ?: return null

        return ParsedUri(queryType, path, authToken)
    }

    private fun getQueryType(uri: Uri): QueryType? {
        val path = uri.path ?: return null

        return if (path.endsWith("/*")) {
            QueryType.DIRECTORY_LIST
        } else {
            QueryType.FILE_INFO
        }
    }

    private fun getAuthToken(uri: Uri): String? {
        val path = uri.path
        if (path.isNullOrEmpty()) {
            return null
        }

        val slashCount = path.count { it == File.separatorChar }
        if (slashCount < 2) {
            return null
        }

        val firstSlashIdx = path.indexOf(File.separatorChar)
        val secondSlashIdx = path.indexOf(File.separatorChar, startIndex = firstSlashIdx + 1)

        val token = path.substring(firstSlashIdx + 1, secondSlashIdx)
        if (token.isEmpty()) {
            return null
        }

        return token
    }

    private fun getPath(uri: Uri): String? {
        val path = uri.path
        if (path.isNullOrEmpty()) {
            return null
        }

        val firstSlashIdx = path.indexOf(File.separatorChar)
        val secondSlashIdx = path.indexOf(File.separatorChar, startIndex = firstSlashIdx + 1)
        val pathWithoutToken = path.substring(secondSlashIdx)
        if (pathWithoutToken.isEmpty()) {
            return null
        }

        return when {
            pathWithoutToken.endsWith("/*") -> {
                if (pathWithoutToken.length > 2) {
                    pathWithoutToken.substringBeforeLast("/*")
                } else {
                    "/"
                }
            }
            else -> pathWithoutToken
        }
    }
}