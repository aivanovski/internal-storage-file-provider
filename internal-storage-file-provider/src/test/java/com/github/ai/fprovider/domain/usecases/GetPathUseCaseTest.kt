package com.github.ai.fprovider.domain.usecases

import com.github.ai.fprovider.data.FileSystem
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.test.TestData.IMAGE_FILE
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.io.FileNotFoundException

class GetPathUseCaseTest {

    private val fileSystem: FileSystem = mockk()
    private val useCase = GetPathUseCase(
        fileSystem = fileSystem
    )

    @Test
    fun `getRealPath should return real path`() {
        // arrange
        val path = IMAGE_FILE.path
        every { fileSystem.getRealPath(path) }.returns(Result.Success(REAL_PATH))

        // act
        val result = useCase.getRealPath(path)

        // assert
        assertThat(result).isEqualTo(Result.Success(REAL_PATH))
    }

    @Test
    fun `getRealPath should return error`() {
        // arrange
        val path = IMAGE_FILE.path
        every { fileSystem.getRealPath(path) }.returns(
            Result.Failure(
                FileNotFoundException()
            )
        )

        // act
        val result = useCase.getRealPath(path)

        // assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.getExceptionOrThrow()).isInstanceOf(FileNotFoundException::class.java)
    }

    companion object {
        private const val REAL_PATH = "reap-path"
    }
}