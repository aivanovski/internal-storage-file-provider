package com.github.ai.fprovider.data.dao

import com.github.ai.fprovider.entity.TokenAndPath

internal interface TokenDao {
    fun add(token: TokenAndPath)
    fun getAll(): List<TokenAndPath>
    fun removeAll()
}