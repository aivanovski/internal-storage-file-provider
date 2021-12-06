package com.github.ai.isfprovider.entity

data class ParsedUri(
    val queryType: QueryType,
    val path: String,
    val authToken: String
)