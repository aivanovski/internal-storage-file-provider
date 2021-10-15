package com.github.ai.fprovider

import android.net.Uri
import com.github.ai.fprovider.UriFactory.createUri
import com.github.ai.fprovider.test.TestData.AUTHORITY
import com.github.ai.fprovider.utils.Constants.CONTENT
import com.github.ai.fprovider.utils.Constants.EMPTY
import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class UriFactoryTest {

    @Test
    fun `createUri should return valid uri`() {
        Truth.assertThat(
            createUri(authority = AUTHORITY, path = PATH, authToken = AUTH_TOKEN)
        ).isEqualTo(URI)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `createUri should throw IllegalArgumentException if authority is empty`() {
        createUri(authority = EMPTY, path = PATH, authToken = AUTH_TOKEN)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `createUri should throw IllegalArgumentException if path is empty`() {
        createUri(authority = AUTHORITY, path = EMPTY, authToken = AUTH_TOKEN)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `createUri should throw IllegalArgumentException if path doesn't start with slash`() {
        createUri(authority = AUTHORITY, path = "image.jpg", authToken = AUTH_TOKEN)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `createUri should throw IllegalArgumentException if authToken is empty`() {
        createUri(authority = AUTHORITY, path = PATH, authToken = EMPTY)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `createUri should throw IllegalArgumentException if authToken doesn't match pattern`() {
        createUri(authority = AUTHORITY, path = PATH, authToken = "abc")
    }

    companion object {
        private const val PATH = "/path"
        private const val AUTH_TOKEN = "token-token-token"
        private val URI = Uri.parse("$CONTENT://$AUTHORITY/$AUTH_TOKEN$PATH")
    }
}