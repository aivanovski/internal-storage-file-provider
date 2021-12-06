package com.github.ai.isfprovider.entity

import com.github.ai.isfprovider.Columns

internal enum class Projection(val columnName: String) {
    URI(Columns.URI),
    NAME(Columns.NAME),
    MIME_TYPE(Columns.MIME_TYPE),
    SIZE(Columns.SIZE);

    companion object {
        fun fromColumnName(columnName: String): Projection? {
            return values().firstOrNull { it.columnName == columnName }
        }
    }
}