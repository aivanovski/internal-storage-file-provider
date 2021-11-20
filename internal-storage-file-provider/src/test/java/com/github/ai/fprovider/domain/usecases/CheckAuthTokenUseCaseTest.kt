package com.github.ai.fprovider.domain.usecases

import com.github.ai.fprovider.data.dao.TokenDao
import com.github.ai.fprovider.entity.TokenAndPath
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class CheckAuthTokenUseCaseTest {

    private val tokenDao: TokenDao = mockk()
    private val useCase = CheckAuthTokenUseCase(tokenDao)

    @Test
    fun `isPathAccessible should return true for root path`() {
        // arrange
        every { tokenDao.getAll() }.returns(TOKEN_DATA_FOR_HOME_DIR)

        // act
        val isAccessible = useCase.isPathAccessible(TOKEN, HOME_DIR)

        // assert
        assertThat(isAccessible).isTrue()
    }

    @Test
    fun `isPathAccessible should return true for path inside root`() {
        // arrange
        every { tokenDao.getAll() }.returns(TOKEN_DATA_FOR_HOME_DIR)

        // act
        val isAccessible = useCase.isPathAccessible(TOKEN, DIR_INSIDE_HOME)

        // assert
        assertThat(isAccessible).isTrue()
    }

    @Test
    fun `isPathAccessible should return true for file`() {
        // arrange
        val data = listOf(
            TokenAndPath(
                authToken = TOKEN,
                rootPath = FILE_INSIDE_HOME
            )
        )
        every { tokenDao.getAll() }.returns(data)

        // act
        val isAccessible = useCase.isPathAccessible(TOKEN, FILE_INSIDE_HOME)

        // assert
        assertThat(isAccessible).isTrue()
    }

    @Test
    fun `isPathAccessible should return false if token is not added`() {
        // arrange
        every { tokenDao.getAll() }.returns(emptyList())

        // act
        val isAccessible = useCase.isPathAccessible(TOKEN, HOME_DIR)

        // assert
        assertThat(isAccessible).isFalse()
    }

    @Test
    fun `isPathAccessible should return false if token is not valid`() {
        // arrange
        every { tokenDao.getAll() }.returns(TOKEN_DATA_FOR_HOME_DIR)

        // act
        val isAccessible = useCase.isPathAccessible(INVALID_TOKEN, HOME_DIR)

        // assert
        assertThat(isAccessible).isFalse()
    }

    companion object {
        private const val TOKEN = "token"
        private const val INVALID_TOKEN = "invalid-token"
        private const val HOME_DIR = "/home"
        private const val DIR_INSIDE_HOME = "/home/dir"
        private const val FILE_INSIDE_HOME = "/home/file.jpg"
        private val TOKEN_DATA_FOR_HOME_DIR = listOf(
            TokenAndPath(TOKEN, HOME_DIR)
        )
    }
}