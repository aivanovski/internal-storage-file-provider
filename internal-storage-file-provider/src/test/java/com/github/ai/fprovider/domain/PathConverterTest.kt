package com.github.ai.fprovider.domain

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
}