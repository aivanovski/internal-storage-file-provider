package com.github.ai.isfprovider

import android.net.Uri
import com.github.ai.isfprovider.domain.AuthTokenValidator
import com.github.ai.isfprovider.utils.Constants.CONTENT

/**
 * Factory to create correct [Uri] for [InternalStorageFileProvider]
 */
object UriFactory {

    /**
     * Creates [Uri] for [InternalStorageFileProvider]
     *
     * @param authority The [android.content.ContentProvider] authority
     * @param path the path to file
     * @param authToken the token that allows access to [path]
     */
    fun createUri(
        authority: String,
        path: String,
        authToken: String
    ): Uri {
        if (authority.isBlank() || path.isBlank() || authToken.isBlank()) {
            throw IllegalArgumentException()
        }

        if (!AuthTokenValidator().isTokenValid(authToken)) {
            throw IllegalArgumentException("'authToken' has invalid format")
        }

        if (!path.startsWith("/")) {
            throw IllegalArgumentException("'path' should start from '/'")
        }

        return Uri.parse("$CONTENT://$authority/$authToken$path")
    }
}