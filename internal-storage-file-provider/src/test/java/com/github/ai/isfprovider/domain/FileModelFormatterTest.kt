package com.github.ai.isfprovider.domain

import com.github.ai.isfprovider.entity.Projection.MIME_TYPE
import com.github.ai.isfprovider.entity.Projection.NAME
import com.github.ai.isfprovider.entity.Projection.SIZE
import com.github.ai.isfprovider.entity.Projection.URI
import com.github.ai.isfprovider.test.TestData.AUTHORITY
import com.github.ai.isfprovider.test.TestData.IMAGE_FILE
import com.github.ai.isfprovider.test.TestData.IMAGE_MIME_TYPE
import com.github.ai.isfprovider.utils.Constants.CONTENT
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
            authority = AUTHORITY,
            projection = projection,
            mimeTypeProvider = mimeTypeProvider
        )

        // assert
        val expectedUri = "$CONTENT://$AUTHORITY${IMAGE_FILE.path}"
        assertThat(row.size).isEqualTo(4)
        assertThat(row[0]).isEqualTo(expectedUri)
        assertThat(row[1]).isEqualTo(IMAGE_FILE.name)
        assertThat(row[2]).isEqualTo(IMAGE_MIME_TYPE)
        assertThat(row[3]).isEqualTo(IMAGE_FILE.size.toString())
    }
}