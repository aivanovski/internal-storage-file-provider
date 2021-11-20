package com.github.ai.fprovider

import com.github.ai.fprovider.data.dao.TokenDao
import com.github.ai.fprovider.domain.AuthTokenValidator
import com.github.ai.fprovider.entity.TokenAndPath
import com.github.ai.fprovider.test.TestData.INVALID_TOKEN
import com.github.ai.fprovider.test.TestData.VALID_TOKEN
import com.github.ai.fprovider.utils.Constants.EMPTY
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
        every { tokenValidator.isTokenValid(VALID_TOKEN) }.returns(false)

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

    companion object {
        private const val PATH = "path"
    }
}