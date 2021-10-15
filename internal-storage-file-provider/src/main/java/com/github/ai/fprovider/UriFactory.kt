package com.github.ai.fprovider

import android.net.Uri
import com.github.ai.fprovider.utils.Constants.CONTENT

object UriFactory {

    private val AUTH_TOKEN_PATTERN = "[\\w\\-]{4,256}".toPattern()

    fun createUri(
        authority: String,
        path: String,
        authToken: String
    ): Uri {
        if (authority.isBlank() || path.isBlank() || authToken.isBlank()) {
            throw IllegalArgumentException()
        }

        if (!AUTH_TOKEN_PATTERN.matcher(authToken).matches()) {
            throw IllegalArgumentException()
        }

        if (!path.startsWith("/")) {
            throw IllegalArgumentException()
        }

        return Uri.parse("$CONTENT://$authority/$authToken$path")
    }
}