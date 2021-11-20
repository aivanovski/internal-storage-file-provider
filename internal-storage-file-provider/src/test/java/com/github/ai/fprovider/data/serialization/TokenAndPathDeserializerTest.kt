package com.github.ai.fprovider.data.serialization

import com.github.ai.fprovider.data.serialization.TokenAndPathSerializer.Companion.SEPARATOR
import com.github.ai.fprovider.test.TestData
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TokenAndPathDeserializerTest {

    @Test
    fun `deserialize should return valid data`() {
        // arrange
        val expectedResult = TestData.TOKEN_FOR_IMAGE
        val input = "${expectedResult.authToken}$SEPARATOR${expectedResult.rootPath}"

        // act
        val result = TokenAndPathDeserializer().deserialize(input)

        // assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `deserialize should return null`() {
        // arrange
        val input = SEPARATOR

        // act
        val result = TokenAndPathDeserializer().deserialize(input)

        // assert
        assertThat(result).isNull()
    }
}