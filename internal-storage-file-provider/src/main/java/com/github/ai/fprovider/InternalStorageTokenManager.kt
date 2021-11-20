package com.github.ai.fprovider

import android.content.Context
import com.github.ai.fprovider.data.dao.TokenDao
import com.github.ai.fprovider.data.dao.TokenDaoImpl
import com.github.ai.fprovider.data.serialization.TokenAndPathDeserializer
import com.github.ai.fprovider.data.serialization.TokenAndPathSerializer
import com.github.ai.fprovider.domain.AuthTokenValidator
import com.github.ai.fprovider.entity.TokenAndPath

class InternalStorageTokenManager internal constructor(
    private val tokenDao: TokenDao,
    private val tokenValidator: AuthTokenValidator
) {

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

    fun removeAllTokens() {
        tokenDao.removeAll()
    }

    companion object {
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