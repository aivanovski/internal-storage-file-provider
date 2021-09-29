package com.github.ai.fprovider.utils

import com.github.ai.fprovider.entity.Table
import com.google.common.truth.Truth.assertThat
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class TableExtKtTest {

    @Test
    fun `toCursor should create Cursor`() {
        // arrange
        val table = Table(
            rows = listOf(ROW1, ROW2),
            columns = listOf(COLUMN1, COLUMN2)
        )

        // act
        val cursor = table.toCursor()

        // assert
        assertThat(cursor.count).isEqualTo(2)
        assertThat(cursor.columnNames).isEqualTo(arrayOf(COLUMN1, COLUMN2))

        cursor.moveToFirst()
        assertThat(cursor.getString(0)).isEqualTo(ROW1[0])
        assertThat(cursor.getString(1)).isEqualTo(ROW1[1])

        cursor.moveToNext()
        assertThat(cursor.getString(0)).isEqualTo(ROW2[0])
        assertThat(cursor.getString(1)).isEqualTo(ROW2[1])
    }

    companion object {
        private const val COLUMN1 = "column1"
        private const val COLUMN2 = "column2"
        private val ROW1 = listOf("row1_column1", "row1_column2")
        private val ROW2 = listOf("row2_column1", "row2_column2")
    }
}