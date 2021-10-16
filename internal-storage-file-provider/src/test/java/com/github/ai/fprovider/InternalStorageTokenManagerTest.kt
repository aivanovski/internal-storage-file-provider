package com.github.ai.fprovider

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.github.ai.fprovider.InternalStorageTokenManager.Companion.KEY_ALL_TOKENS
import com.github.ai.fprovider.InternalStorageTokenManager.Companion.SHARED_PREFS_NAME
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class InternalStorageTokenManagerTest {

    private lateinit var tokenManager: InternalStorageTokenManager
    private lateinit var context: Context
    private lateinit var preferences: SharedPreferences

    @Before
    fun setUp() {
        val context = Robolectric
            .buildActivity(AppCompatActivity::class.java)
            .get() as Context

        this.context = context
        this.preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        tokenManager = InternalStorageTokenManager(context)
    }

    @Test
    fun `addToken should store token`() {
        // arrange
        assertThat(preferences.getStringSet(KEY_ALL_TOKENS, null)).isNull()

        // act
        tokenManager.addToken(TOKEN)

        // assert
        assertThat(preferences.getStringSet(KEY_ALL_TOKENS, null)).isEqualTo(setOf(TOKEN))
    }

    @Test
    fun `removeAllTokens should remove all tokens`() {
        // arrange
        tokenManager.addToken(TOKEN)
        assertThat(preferences.getStringSet(KEY_ALL_TOKENS, null)).isEqualTo(setOf(TOKEN))

        // act
        tokenManager.removeAllTokens()

        // assert
        assertThat(preferences.getStringSet(KEY_ALL_TOKENS, null)).isEqualTo(emptySet<String>())
    }

    @Test
    fun `isTokenValid should return true`() {
        tokenManager.addToken(TOKEN)
        assertThat(tokenManager.isTokenValid(TOKEN)).isTrue()
    }

    @Test
    fun `isTokenValid should return false`() {
        assertThat(tokenManager.isTokenValid(TOKEN)).isFalse()
    }

    companion object {
        private const val TOKEN = "token-token-token-token"
    }
}