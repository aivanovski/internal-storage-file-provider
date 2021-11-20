package com.github.ai.fprovider.data.serialization

import com.github.ai.fprovider.data.serialization.TokenAndPathSerializer.Companion.SEPARATOR
import com.github.ai.fprovider.test.TestData
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TokenAndPathSerializerTest {

    @Test
    fun `serialize should return valid string`() {
        // arrange
        val data = TestData.TOKEN_FOR_IMAGE
        val expectedResult = "${data.authToken}$SEPARATOR${data.rootPath}"

        // act
        val result = TokenAndPathSerializer().serialize(data)

        // assert
        assertThat(result).isEqualTo(expectedResult)
    }
}