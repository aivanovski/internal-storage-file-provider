package com.github.ai.fprovider.entity

data class ParsedUri(
    val queryType: QueryType,
    val path: String,
    val authToken: String
)