package com.github.ai.fprovider.domain

import com.github.ai.fprovider.domain.usecases.GetMimeTypeUseCase
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.entity.exception.InvalidPathException
import com.github.ai.fprovider.test.createMockedUri
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class InteractorTest {

    private val pathConverter: PathConverter = mockk()
    private val mimeTypeUseCase: GetMimeTypeUseCase = mockk()
    private val interactor = Interactor(
        pathConverter = pathConverter,
        mimeTypeUseCase = mimeTypeUseCase
    )

    @Test
    fun `getMimeType should send uri to PathConverter and ask GetMimeTypeUseCase`() {
        // arrange
        val uri = createMockedUri(
            path = PATH
        )
        every { pathConverter.getPath(uri) }.returns(PATH)
        every { mimeTypeUseCase.getMimeType(PATH) }.returns(Result.Success(RESULT))

        // act
        val mimeType = interactor.getMimeType(uri)

        // assert
        assertThat(mimeType).isEqualTo(Result.Success(RESULT))
    }

    @Test
    fun `getMimeType should return InvalidPathException`() {
        // arrange
        val uri = createMockedUri(
            path = null
        )
        every { pathConverter.getPath(uri) }.returns(null)

        // act
        val mimeType = interactor.getMimeType(uri)

        // assert
        assertThat(mimeType.isFailure).isEqualTo(true)
        assertThat(mimeType.getExceptionOrThrow()).isInstanceOf(InvalidPathException::class.java)
    }

    companion object {
        private const val PATH = "/path/placeholder"
        private const val RESULT = "result-placeholder"
    }
}