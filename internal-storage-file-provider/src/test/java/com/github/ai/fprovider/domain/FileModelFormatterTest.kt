package com.github.ai.fprovider.domain

import com.github.ai.fprovider.entity.Projection.MIME_TYPE
import com.github.ai.fprovider.entity.Projection.NAME
import com.github.ai.fprovider.entity.Projection.SIZE
import com.github.ai.fprovider.entity.Projection.URI
import com.github.ai.fprovider.test.TestData.AUTHORITY
import com.github.ai.fprovider.test.TestData.IMAGE_FILE
import com.github.ai.fprovider.test.TestData.IMAGE_MIME_TYPE
import com.github.ai.fprovider.utils.Constants.CONTENT
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class FileModelFormatterTest {

    private val mimeTypeProvider: MimeTypeProvider = mockk()
    private val fileModelFormatter = FileModelFormatter()

    @Test
    fun `format should convert data into list`() {
        // arrange
        val projection = listOf(URI, NAME, MIME_TYPE, SIZE)
        every { mimeTypeProvider.getMimeType(IMAGE_FILE) }.returns(IMAGE_MIME_TYPE)

        // act
        val row = fileModelFormatter.format(
            file = IMAGE_FILE,
            rootPath = ROOT_PATH,
            authority = AUTHORITY,
            projection = projection,
            mimeTypeProvider = mimeTypeProvider
        )

        // assert
        val expectedUri = "$CONTENT://$AUTHORITY$ROOT_PATH${IMAGE_FILE.path}"
        assertThat(row.size).isEqualTo(4)
        assertThat(row[0]).isEqualTo(expectedUri)
        assertThat(row[1]).isEqualTo(IMAGE_FILE.name)
        assertThat(row[2]).isEqualTo(IMAGE_MIME_TYPE)
        assertThat(row[3]).isEqualTo(IMAGE_FILE.size.toString())
    }

    companion object {
        private const val ROOT_PATH = "/data/data/com.test"
    }
}