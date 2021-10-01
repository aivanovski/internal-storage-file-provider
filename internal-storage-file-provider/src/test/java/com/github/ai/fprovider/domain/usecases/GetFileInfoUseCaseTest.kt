package com.github.ai.fprovider.domain.usecases

import com.github.ai.fprovider.data.FileSystem
import com.github.ai.fprovider.domain.FileModelFormatter
import com.github.ai.fprovider.domain.MimeTypeProvider
import com.github.ai.fprovider.entity.Projection
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.entity.Table
import com.github.ai.fprovider.test.TestData.AUTHORITY
import com.github.ai.fprovider.test.TestData.IMAGE_FILE
import com.github.ai.fprovider.utils.toColumnNames
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.io.FileNotFoundException

class GetFileInfoUseCaseTest {

    private val fileSystem: FileSystem = mockk()
    private val mimeTypeProvider: MimeTypeProvider = mockk()
    private val fileModelFormatter: FileModelFormatter = mockk()
    private val useCase = GetFileInfoUseCase(
        fileSystem = fileSystem,
        mimeTypeProvider = mimeTypeProvider,
        fileModelFormatter = fileModelFormatter,
        authority = AUTHORITY
    )

    @Test
    fun `getFileInfo should return information about file`() {
        // arrange
        val path = IMAGE_FILE.path
        every { fileSystem.getFile(path) }.returns(Result.Success(IMAGE_FILE))
        every {
            fileModelFormatter.format(
                IMAGE_FILE,
                AUTHORITY,
                PROJECTION,
                mimeTypeProvider
            )
        }.returns(FILE_INFO)

        // act
        val result = useCase.getFileInto(path, PROJECTION)

        // assert
        assertThat(result).isEqualTo(Result.Success(EXPECTED_TABLE))
    }

    @Test
    fun `getFileInfo should return exception from FileSystem`() {
        // arrange
        val path = IMAGE_FILE.path
        every { fileSystem.getFile(path) }.returns(Result.Failure(FileNotFoundException()))

        // act
        val result = useCase.getFileInto(path, PROJECTION)

        // assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.getExceptionOrThrow()).isInstanceOf(FileNotFoundException::class.java)
    }

    companion object {
        private val PROJECTION = Projection.values().toList()
        private val FILE_INFO = listOf(
            "uri-placeholder",
            "name-placeholder",
            "mime-type-placeholder",
            "size-placeholder"
        )
        private val EXPECTED_TABLE = Table(
            rows = listOf(FILE_INFO),
            columns = PROJECTION.toColumnNames()
        )
    }
}