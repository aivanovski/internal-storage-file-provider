package com.github.ai.fprovider.entity

import com.github.ai.fprovider.Columns

internal enum class Projection(val columnName: String) {
    NAME(Columns.NAME),
    MIME_TYPE(Columns.MIME_TYPE),
    SIZE(Columns.SIZE);

    companion object {
        fun fromColumnName(columnName: String): Projection? {
            return values().firstOrNull { it.columnName == columnName }
        }
    }
}