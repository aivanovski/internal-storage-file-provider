package com.github.ai.isfprovider.data.serialization

import com.github.ai.isfprovider.entity.TokenAndPath

internal class TokenAndPathSerializer : Serializer<TokenAndPath> {

    override fun serialize(data: TokenAndPath): String {
        return "${data.authToken}$SEPARATOR${data.rootPath}"
    }

    companion object {
        const val SEPARATOR = "#"
    }
}