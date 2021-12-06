package com.github.ai.isfprovider.utils

import com.github.ai.isfprovider.entity.Projection.MIME_TYPE
import com.github.ai.isfprovider.entity.Projection.NAME
import com.github.ai.isfprovider.entity.Projection.SIZE
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ProjectionExtKtTest {

    @Test
    fun `toColumnNames should convert to column names correctly`() {
        // arrange
        val projection = listOf(SIZE, NAME, MIME_TYPE, SIZE)
        val expectedColumns = projection.map { it.columnName }

        // act
        val columns = projection.toColumnNames()

        // assert
        assertThat(columns).isEqualTo(expectedColumns)
    }
}