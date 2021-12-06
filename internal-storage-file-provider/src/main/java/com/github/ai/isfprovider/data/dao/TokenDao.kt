package com.github.ai.isfprovider.data.dao

import com.github.ai.isfprovider.entity.TokenAndPath

internal interface TokenDao {
    fun add(token: TokenAndPath)
    fun getAll(): List<TokenAndPath>
    fun remove(token: String)
    fun removeAll()
}