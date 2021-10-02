package com.github.ai.fprovider.domain.usecases

import com.github.ai.fprovider.data.FileSystem
import com.github.ai.fprovider.domain.FileModelFormatter
import com.github.ai.fprovider.domain.MimeTypeProvider
import com.github.ai.fprovider.entity.Projection
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.entity.Table
import com.github.ai.fprovider.test.TestData.AUTHORITY
import com.github.ai.fprovider.test.TestData.DIRECTORY_FILE
import com.github.ai.fprovider.test.TestData.IMAGE_FILE
import com.github.ai.fprovider.test.TestData.PARENT_FILE
import com.github.ai.fprovider.utils.toColumnNames
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.io.FileNotFoundException

class GetDirectoryListUseCaseTest {

    private val fileSystem: FileSystem = mockk()
    private val mimeTypeProvider: MimeTypeProvider = mockk()
    private val fileModelFormatter: FileModelFormatter = mockk()
    private val useCase = GetDirectoryListUseCase(
        fileSystem = fileSystem,
        fileModelFormatter = fileModelFormatter,
        mimeTypeProvider = mimeTypeProvider,
        authority = AUTHORITY
    )

    @Test
    fun `getDirectoryList should return list of files`() {
        // arrange
        val path = PARENT_FILE.path
        val files = listOf(IMAGE_FILE, DIRECTORY_FILE)
        every { fileSystem.getChildFiles(path) }.returns(Result.Success(files))
        every {
            fileModelFormatter.format(
                IMAGE_FILE,
                AUTHORITY,
                PROJECTION,
                mimeTypeProvider
            )
        }.returns(IMAGE_FILE_INFO)
        every {
            fileModelFormatter.format(
                DIRECTORY_FILE,
                AUTHORITY,
                PROJECTION,
                mimeTypeProvider
            )
        }.returns(DIRECTORY_FILE_INFO)

        // act
        val result = useCase.getDirectoryList(path, PROJECTION)

        // assert
        assertThat(result).isEqualTo(Result.Success(EXPECTED_TABLE))
    }

    @Test
    fun `getDirectoryList should return exception from FileSystem`() {
        // arrange
        val path = PARENT_FILE.path
        every { fileSystem.getChildFiles(path) }.returns(Result.Failure(FileNotFoundException()))

        // act
        val result = useCase.getDirectoryList(path, PROJECTION)

        // assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.getExceptionOrThrow()).isInstanceOf(FileNotFoundException::class.java)
    }

    companion object {
        private val PROJECTION = Projection.values().toList()
        private val IMAGE_FILE_INFO = PROJECTION.map { "$it-file-placeholder" }
        private val DIRECTORY_FILE_INFO = PROJECTION.map { "$it-dir-placeholder" }
        private val EXPECTED_TABLE = Table(
            rows = listOf(IMAGE_FILE_INFO, DIRECTORY_FILE_INFO),
            columns = PROJECTION.toColumnNames()
        )
    }
}