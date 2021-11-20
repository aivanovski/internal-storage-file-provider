package com.github.ai.fprovider.data.serialization

import com.github.ai.fprovider.entity.TokenAndPath

internal class TokenAndPathSerializer : Serializer<TokenAndPath> {

    override fun serialize(data: TokenAndPath): String {
        return "${data.authToken}$SEPARATOR${data.rootPath}"
    }

    companion object {
        const val SEPARATOR = "#"
    }
}