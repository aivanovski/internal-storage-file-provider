package com.github.ai.fprovider.domain

import com.github.ai.fprovider.entity.ParsedUri
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
class UriParserTest {

    private val uriParser = UriParser()

    @Test
    fun `parse should return data for file`() {
        // arrange
        val uri = IMAGE_FILE.toUri()

        // act
        val result = uriParser.parse(uri)

        // assert
        assertThat(result).isEqualTo(
            ParsedUri(
                queryType = QueryType.FILE_INFO,
                path = IMAGE_FILE.path
            )
        )
    }

    @Test
    fun `parse should return data for directory`() {
        // arrange
        val uri = createUri(path = "${DIRECTORY_FILE.path}/*")

        // act
        val result = uriParser.parse(uri)

        // assert
        assertThat(result).isEqualTo(
            ParsedUri(
                queryType = QueryType.DIRECTORY_LIST,
                path = DIRECTORY_FILE.path
            )
        )
    }

    @Test
    fun `parse should return data for root file`() {
        // arrange
        val uri = createUri(path = ROOT)

        // act
        val result = uriParser.parse(uri)

        // assert
        assertThat(result).isEqualTo(
            ParsedUri(
                queryType = QueryType.FILE_INFO,
                path = ROOT
            )
        )
    }

    @Test
    fun `parse should return data for root directory`() {
        // arrange
        val uri = createUri(path = "/*")

        // act
        val result = uriParser.parse(uri)

        // assert
        assertThat(result).isEqualTo(
            ParsedUri(
                queryType = QueryType.DIRECTORY_LIST,
                path = ROOT
            )
        )
    }

    @Test
    fun `parse should return null if path is empty`() {
        // arrange
        val uri = createUri(path = "")

        // act
        val result = uriParser.parse(uri)

        // assert
        assertThat(result).isNull()
    }
}