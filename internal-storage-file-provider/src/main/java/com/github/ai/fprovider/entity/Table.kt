package com.github.ai.fprovider.entity

internal data class Table(
    val rows: List<List<String>> = emptyList(),
    val columns: List<String> = emptyList()
)