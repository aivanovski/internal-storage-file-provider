package com.github.ai.fprovider.domain

import com.github.ai.fprovider.entity.QueryType
import com.github.ai.fprovider.test.TestData.DIRECTORY_FILE
import com.github.ai.fprovider.test.TestData.IMAGE_FILE
import com.github.ai.fprovider.test.createMockedUri
import com.github.ai.fprovider.utils.Constants.ROOT
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PathConverterTest {

    private val pathConverter = PathConverter()

    @Test
    fun `getPath should return path for file`() {
        // arrange
        val uri = createMockedUri(
            path = IMAGE_FILE.path
        )

        // act
        val path = pathConverter.getPath(uri)

        // assert
        assertThat(path).isEqualTo(IMAGE_FILE.path)
    }

    @Test
    fun `getPath should return path for directory`() {
        // arrange
        val uri = createMockedUri(
            path = "${DIRECTORY_FILE.path}/*"
        )

        // act
        val path = pathConverter.getPath(uri)

        // assert
        assertThat(path).isEqualTo(DIRECTORY_FILE.path)
    }

    @Test
    fun `getPath should return path for root`() {
        // arrange
        val uri = createMockedUri(
            path = "/*"
        )

        // act
        val path = pathConverter.getPath(uri)

        // assert
        assertThat(path).isEqualTo(ROOT)
    }

    @Test
    fun `getPath should return null`() {
        // arrange
        val uri = createMockedUri(
            path = ""
        )

        // act
        val path = pathConverter.getPath(uri)

        // assert
        assertThat(path).isNull()
    }

    @Test
    fun `getQueryType should return type for file`() {
        // arrange
        val uri = createMockedUri(
            path = "/image.jpg"
        )

        // act
        val queryType = pathConverter.getQueryType(uri)

        // assert
        assertThat(queryType).isEqualTo(QueryType.FILE_INFO)
    }

    @Test
    fun `getQueryType should return type for directory listing`() {
        // arrange
        val uri = createMockedUri(
            path = "/home/*"
        )

        // act
        val queryType = pathConverter.getQueryType(uri)

        // assert
        assertThat(queryType).isEqualTo(QueryType.DIRECTORY_LIST)
    }

    @Test
    fun `getQueryType should return null`() {
        // arrange
        val uri = createMockedUri(
            path = null
        )

        // act
        val queryType = pathConverter.getQueryType(uri)

        // assert
        assertThat(queryType).isNull()
    }
}