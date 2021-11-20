package com.github.ai.fprovider

import android.net.Uri
import com.github.ai.fprovider.domain.AuthTokenValidator
import com.github.ai.fprovider.utils.Constants.CONTENT

object UriFactory {

    fun createUri(
        authority: String,
        path: String,
        authToken: String
    ): Uri {
        if (authority.isBlank() || path.isBlank() || authToken.isBlank()) {
            throw IllegalArgumentException()
        }

        if (!AuthTokenValidator().isTokenValid(authToken)) {
            throw IllegalArgumentException()
        }

        if (!path.startsWith("/")) {
            throw IllegalArgumentException()
        }

        return Uri.parse("$CONTENT://$authority/$authToken$path")
    }
}