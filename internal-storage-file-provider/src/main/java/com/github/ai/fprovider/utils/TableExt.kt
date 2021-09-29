package com.github.ai.fprovider.utils

import android.database.Cursor
import android.database.MatrixCursor
import com.github.ai.fprovider.entity.Table

internal fun Table.toCursor(): Cursor {
    return MatrixCursor(columns.toTypedArray(), rows.size).apply {
        for (row in rows) {
            addRow(row.toTypedArray())
        }
    }
}