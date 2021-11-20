package com.github.ai.fprovider.domain

import com.github.ai.fprovider.test.TestData.INVALID_TOKEN
import com.github.ai.fprovider.test.TestData.VALID_TOKEN
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AuthTokenValidatorTest {

    @Test
    fun `isTokenValid should return true`() {
        assertThat(AuthTokenValidator().isTokenValid(VALID_TOKEN))
            .isTrue()
    }

    @Test
    fun `isTokenValid should return false`() {
        assertThat(AuthTokenValidator().isTokenValid(INVALID_TOKEN))
            .isFalse()
    }
}