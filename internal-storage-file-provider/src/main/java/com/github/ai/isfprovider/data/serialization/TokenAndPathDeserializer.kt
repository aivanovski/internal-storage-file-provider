package com.github.ai.isfprovider.data.serialization

import com.github.ai.isfprovider.entity.TokenAndPath

internal class TokenAndPathDeserializer : Deserializer<TokenAndPath> {

    override fun deserialize(data: String): TokenAndPath? {
        val values = data.split(TokenAndPathSerializer.SEPARATOR)
        if (values.size != 2 || values.any { it.isEmpty() }) {
            return null
        }

        return TokenAndPath(
            authToken = values[0],
            rootPath = values[1]
        )
    }
}