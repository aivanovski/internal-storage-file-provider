package com.github.ai.isfprovider.domain.usecases

import com.github.ai.isfprovider.data.FileSystem
import com.github.ai.isfprovider.domain.MimeTypeProvider
import com.github.ai.isfprovider.entity.FileModel
import com.github.ai.isfprovider.entity.Result
import com.github.ai.isfprovider.test.TestData.DIRECTORY_FILE
import com.github.ai.isfprovider.test.TestData.DIRECTORY_MIME_TYPE
import com.github.ai.isfprovider.test.TestData.IMAGE_FILE
import com.github.ai.isfprovider.test.TestData.IMAGE_MIME_TYPE
import com.github.ai.isfprovider.utils.Constants.EMPTY
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.io.FileNotFoundException

class GetMimeTypeUseCaseTest {

    private val fileSystem: FileSystem = mockk()
    private val mimeTypeProvider: MimeTypeProvider = mockk()
    private val useCase = GetMimeTypeUseCase(
        fileSystem = fileSystem,
        mimeTypeProvider = mimeTypeProvider
    )

    @Test
    fun `getMimeType should return mimeType for file`() {
        // arrange
        val file = IMAGE_FILE
        every { fileSystem.getFile(file.path) }.returns(Result.Success(file))
        every { mimeTypeProvider.getMimeType(file) }.returns(IMAGE_MIME_TYPE)

        // act
        val mimeType = useCase.getMimeType(file.path)

        // assert
        assertThat(mimeType).isEqualTo(Result.Success(IMAGE_MIME_TYPE))
    }

    @Test
    fun `getMimeType should return mimeType for directory`() {
        // arrange
        val file = DIRECTORY_FILE
        every { fileSystem.getFile(file.path) }.returns(Result.Success(file))
        every { mimeTypeProvider.getMimeType(file) }.returns(DIRECTORY_MIME_TYPE)

        // act
        val mimeType = useCase.getMimeType(file.path)

        // assert
        assertThat(mimeType).isEqualTo(Result.Success(DIRECTORY_MIME_TYPE))
    }

    @Test
    fun `getMimeType should return empty string for unknown file`() {
        // arrange
        val file = FileModel(
            path = PATH,
            name = "abc",
            size = 12345L,
            isDirectory = false
        )
        every { fileSystem.getFile(file.path) }.returns(Result.Success(file))
        every { mimeTypeProvider.getMimeType(file) }.returns(null)

        // act
        val mimeType = useCase.getMimeType(file.path)

        // assert
        assertThat(mimeType).isEqualTo(Result.Success(EMPTY))
    }

    @Test
    fun `getMimeType should return error`() {
        // arrange
        every { fileSystem.getFile(PATH) }.returns(Result.Failure(FileNotFoundException()))

        // act
        val mimeType = useCase.getMimeType(PATH)

        // assert
        assertThat(mimeType.isFailure).isEqualTo(true)
        assertThat(mimeType.getExceptionOrThrow()).isInstanceOf(FileNotFoundException::class.java)
    }

    companion object {
        private const val PATH = "/path"
    }
}