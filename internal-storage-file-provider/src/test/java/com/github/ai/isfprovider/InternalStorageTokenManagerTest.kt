package com.github.ai.isfprovider

import com.github.ai.isfprovider.data.dao.TokenDao
import com.github.ai.isfprovider.domain.AuthTokenValidator
import com.github.ai.isfprovider.entity.TokenAndPath
import com.github.ai.isfprovider.test.TestData.INVALID_TOKEN
import com.github.ai.isfprovider.test.TestData.TOKEN_FOR_DIRECTORY
import com.github.ai.isfprovider.test.TestData.TOKEN_FOR_IMAGE
import com.github.ai.isfprovider.test.TestData.VALID_TOKEN
import com.github.ai.isfprovider.utils.Constants.EMPTY
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class InternalStorageTokenManagerTest {

    private val tokenDao: TokenDao = mockk()
    private val tokenValidator: AuthTokenValidator = mockk()
    private val tokenManager = InternalStorageTokenManager(
        tokenDao = tokenDao,
        tokenValidator = tokenValidator
    )

    @Test
    fun `addToken should call dao`() {
        // arrange
        val token = TokenAndPath(VALID_TOKEN, PATH)
        every { tokenDao.add(token) }.returns(Unit)
        every { tokenValidator.isTokenValid(VALID_TOKEN) }.returns(true)

        // act
        tokenManager.addToken(VALID_TOKEN, PATH)

        // assert
        verify { tokenDao.add(token) }
        confirmVerified(tokenDao)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `addToken should return IllegalArgumentException if token is invalid`() {
        // arrange
        every { tokenValidator.isTokenValid(INVALID_TOKEN) }.returns(false)

        // act
        tokenManager.addToken(INVALID_TOKEN, PATH)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `addToken should return IllegalArgumentException if path is invalid`() {
        // arrange
        every { tokenValidator.isTokenValid(VALID_TOKEN) }.returns(true)

        // act
        tokenManager.addToken(VALID_TOKEN, EMPTY)
    }

    @Test
    fun `removeAllTokens should call dao`() {
        // arrange
        every { tokenDao.removeAll() }.returns(Unit)

        // act
        tokenManager.removeAllTokens()

        // assert
        verify { tokenDao.removeAll() }
        confirmVerified(tokenDao)
    }

    @Test
    fun `remove should call dao`() {
        // arrange
        every { tokenDao.remove(VALID_TOKEN) }.returns(Unit)

        // act
        tokenManager.removeToken(VALID_TOKEN)

        // assert
        verify { tokenDao.remove(VALID_TOKEN) }
        confirmVerified(tokenDao)
    }

    @Test
    fun `getPathByToken should return associated path`() {
        // arrange
        every { tokenDao.getAll() }.returns(listOf(TOKEN_FOR_IMAGE, TOKEN_FOR_DIRECTORY))

        // act
        val path = tokenManager.getPathByToken(TOKEN_FOR_IMAGE.authToken)

        // assert
        assertThat(path).isEqualTo(TOKEN_FOR_IMAGE.rootPath)
    }

    @Test
    fun `getAllTokens should return all tokens`() {
        // arrange
        every { tokenDao.getAll() }.returns(listOf(TOKEN_FOR_IMAGE, TOKEN_FOR_DIRECTORY))

        // act
        val tokens = tokenManager.getAllTokens()

        // assert
        val expectedTokens = listOf(TOKEN_FOR_IMAGE, TOKEN_FOR_DIRECTORY)
            .map { it.authToken }
        assertThat(tokens).isEqualTo(expectedTokens)
    }

    companion object {
        private const val PATH = "path"
    }
}