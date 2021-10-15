package com.github.ai.fprovider.domain

import android.net.Uri
import com.github.ai.fprovider.entity.ParsedUri
import com.github.ai.fprovider.entity.QueryType
import com.github.ai.fprovider.test.TestData.AUTHORITY
import com.github.ai.fprovider.test.TestData.AUTH_TOKEN
import com.github.ai.fprovider.test.TestData.DIRECTORY_FILE
import com.github.ai.fprovider.test.TestData.IMAGE_FILE
import com.github.ai.fprovider.test.createUri
import com.github.ai.fprovider.utils.Constants.EMPTY
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
        val uri = createUri(path = IMAGE_FILE.path, authToken = AUTH_TOKEN)

        // act
        val result = uriParser.parse(uri)

        // assert
        assertThat(result).isEqualTo(
            ParsedUri(
                queryType = QueryType.FILE_INFO,
                path = IMAGE_FILE.path,
                authToken = AUTH_TOKEN
            )
        )
    }

    @Test
    fun `parse should return data for directory`() {
        // arrange
        val uri = createUri(path = "${DIRECTORY_FILE.path}/*", authToken = AUTH_TOKEN)

        // act
        val result = uriParser.parse(uri)

        // assert
        assertThat(result).isEqualTo(
            ParsedUri(
                queryType = QueryType.DIRECTORY_LIST,
                path = DIRECTORY_FILE.path,
                authToken = AUTH_TOKEN
            )
        )
    }

    @Test
    fun `parse should return data for root file`() {
        // arrange
        val uri = createUri(path = ROOT, authToken = AUTH_TOKEN)

        // act
        val result = uriParser.parse(uri)

        // assert
        assertThat(result).isEqualTo(
            ParsedUri(
                queryType = QueryType.FILE_INFO,
                path = ROOT,
                authToken = AUTH_TOKEN
            )
        )
    }

    @Test
    fun `parse should return data for root directory`() {
        // arrange
        val uri = createUri(path = "/*", authToken = AUTH_TOKEN)

        // act
        val result = uriParser.parse(uri)

        // assert
        assertThat(result).isEqualTo(
            ParsedUri(
                queryType = QueryType.DIRECTORY_LIST,
                path = ROOT,
                authToken = AUTH_TOKEN
            )
        )
    }

    @Test
    fun `parse should return null if path is empty`() {
        // arrange
        val uri = createUri(path = EMPTY, authToken = AUTH_TOKEN)

        // act
        val result = uriParser.parse(uri)

        // assert
        assertThat(result).isNull()
    }

    @Test
    fun `parse should return null if authToken is empty`() {
        // arrange
        val uri = createUri(path = EMPTY, authToken = EMPTY)

        // act
        val result = uriParser.parse(uri)

        // assert
        assertThat(result).isNull()
    }

    @Test
    fun `parse should return null if authToken is missed`() {
        // arrange
        val uri = Uri.parse("content://$AUTHORITY/path")

        // act
        val result = uriParser.parse(uri)

        // assert
        assertThat(result).isNull()
    }
}