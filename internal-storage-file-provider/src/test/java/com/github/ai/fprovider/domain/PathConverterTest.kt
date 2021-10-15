package com.github.ai.fprovider.domain

import com.github.ai.fprovider.entity.QueryType
import com.github.ai.fprovider.test.TestData.DIRECTORY_FILE
import com.github.ai.fprovider.test.TestData.IMAGE_FILE
import com.github.ai.fprovider.test.createUri
import com.github.ai.fprovider.test.toUri
import com.github.ai.fprovider.utils.Constants.ROOT
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class PathConverterTest {

    private val pathConverter = PathConverter()

    @Test
    fun `getPath should return path for file`() {
        // arrange
        val uri = IMAGE_FILE.toUri()

        // act
        val path = pathConverter.getPath(uri)

        // assert
        assertThat(path).isEqualTo(IMAGE_FILE.path)
    }

    @Test
    fun `getPath should return path for directory`() {
        // arrange
        val uri = createUri(
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
        val uri = createUri(
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
        val uri = createUri(
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
        val uri = createUri(
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
        val uri = createUri(
            path = "/home/*"
        )

        // act
        val queryType = pathConverter.getQueryType(uri)

        // assert
        assertThat(queryType).isEqualTo(QueryType.DIRECTORY_LIST)
    }
}