package com.github.ai.isfprovider.utils

import android.database.Cursor
import android.database.MatrixCursor
import com.github.ai.isfprovider.entity.Table

internal fun Table.toCursor(): Cursor {
    return MatrixCursor(columns.toTypedArray(), rows.size).apply {
        for (row in rows) {
            addRow(row.toTypedArray())
        }
    }
}