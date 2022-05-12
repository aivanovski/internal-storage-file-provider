package com.github.ai.isfprovider

import android.content.Context
import com.github.ai.isfprovider.data.dao.TokenDao
import com.github.ai.isfprovider.data.dao.TokenDaoImpl
import com.github.ai.isfprovider.data.serialization.TokenAndPathDeserializer
import com.github.ai.isfprovider.data.serialization.TokenAndPathSerializer
import com.github.ai.isfprovider.domain.AuthTokenValidator
import com.github.ai.isfprovider.entity.TokenAndPath

/**
 * Manages access tokens for [InternalStorageFileProvider]
 */
class InternalStorageTokenManager internal constructor(
    private val tokenDao: TokenDao,
    private val tokenValidator: AuthTokenValidator
) {

    /**
     * Saves [token] that allows access to file in [rootPath]
     *
     * @param token access token
     * @param rootPath relative path to file or directory inside internal storage
     */
    fun addToken(token: String, rootPath: String) {
        if (!tokenValidator.isTokenValid(token)) {
            throw IllegalArgumentException()
        }

        if (rootPath.isEmpty()) {
            throw IllegalArgumentException()
        }

        tokenDao.add(
            TokenAndPath(
                authToken = token,
                rootPath = rootPath
            )
        )
    }

    /**
     * Returns path that is associated with specified [token]
     *
     * @param token access token
     * @return relative path to file or directory inside internal storage
     */
    fun getPathByToken(token: String): String? {
        return tokenDao.getAll()
            .filter { it.authToken == token }
            .map { it.rootPath }
            .firstOrNull()
    }

    /**
     * Removes saved [token] and associated with it path
     *
     * @param token access token
     */
    fun removeToken(token: String) {
        return tokenDao.remove(token)
    }

    /**
     * Removes all saved tokens
     */
    fun removeAllTokens() {
        tokenDao.removeAll()
    }

    /**
     * Return all saved tokens
     */
    fun getAllTokens(): List<String> {
        return tokenDao.getAll()
            .map { it.authToken }
    }

    companion object {

        /**
         * Creates new instance of [InternalStorageTokenManager]
         *
         * @param context application context
         */
        fun from(context: Context): InternalStorageTokenManager {
            return InternalStorageTokenManager(
                tokenDao = TokenDaoImpl(
                    context,
                    TokenAndPathSerializer(),
                    TokenAndPathDeserializer()
                ),
                tokenValidator = AuthTokenValidator()
            )
        }
    }
}