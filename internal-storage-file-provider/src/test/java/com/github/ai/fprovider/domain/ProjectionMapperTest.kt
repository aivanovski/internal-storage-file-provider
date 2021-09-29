package com.github.ai.fprovider.domain

import com.github.ai.fprovider.entity.Projection
import com.github.ai.fprovider.entity.Projection.MIME_TYPE
import com.github.ai.fprovider.entity.Projection.NAME
import com.github.ai.fprovider.entity.Projection.SIZE
import com.github.ai.fprovider.entity.Projection.URI
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ProjectionMapperTest {

    private val projectionMapper = ProjectionMapper()

    @Test
    fun `getProjectionFromColumns should return all columns if columns is empty`() {
        // arrange

        // act
        val projection = projectionMapper.getProjectionFromColumns(emptyList())

        // assert
        assertThat(projection.size).isEqualTo(ALL_COLUMNS.size)
        assertThat(projection.containsAll(ALL_COLUMNS)).isTrue()
    }

    @Test
    fun `getProjectionFromColumns should return columns in dedicated order`() {
        // arrange
        val columns = listOf(SIZE, MIME_TYPE, URI, NAME).map { it.columnName }

        // act
        val projection = projectionMapper.getProjectionFromColumns(columns)

        // assert
        assertThat(projection.size).isEqualTo(4)
        assertThat(projection[0]).isEqualTo(SIZE)
        assertThat(projection[1]).isEqualTo(MIME_TYPE)
        assertThat(projection[2]).isEqualTo(URI)
        assertThat(projection[3]).isEqualTo(NAME)
    }

    @Test
    fun `getProjectionFromColumns should ignore unknown columns`() {
        // arrange
        val columns = listOf(
            UNKNOWN_COLUMN,
            NAME.columnName,
            MIME_TYPE.columnName
        )

        // act
        val projection = projectionMapper.getProjectionFromColumns(columns)

        // assert
        assertThat(projection.size).isEqualTo(2)
        assertThat(projection[0]).isEqualTo(NAME)
        assertThat(projection[1]).isEqualTo(MIME_TYPE)
    }

    companion object {
        private val ALL_COLUMNS = Projection.values().toList()
        private const val UNKNOWN_COLUMN = "unknown_column"
    }
}