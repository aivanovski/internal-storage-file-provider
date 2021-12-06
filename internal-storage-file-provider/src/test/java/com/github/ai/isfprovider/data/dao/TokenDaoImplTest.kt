package com.github.ai.isfprovider.data.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.github.ai.isfprovider.data.dao.TokenDaoImpl.Companion.KEY_TOKENS_DATA
import com.github.ai.isfprovider.data.dao.TokenDaoImpl.Companion.SHARED_PREFS_NAME
import com.github.ai.isfprovider.data.serialization.Deserializer
import com.github.ai.isfprovider.data.serialization.Serializer
import com.github.ai.isfprovider.entity.TokenAndPath
import com.github.ai.isfprovider.test.TestData.TOKEN_FOR_DIRECTORY
import com.github.ai.isfprovider.test.TestData.TOKEN_FOR_IMAGE
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class TokenDaoImplTest {

    private val serializer: Serializer<TokenAndPath> = mockk()
    private val deserializer: Deserializer<TokenAndPath> = mockk()
    private lateinit var preferences: SharedPreferences
    private lateinit var dao: TokenDao

    @Before
    fun setUp() {
        val context = Robolectric
            .buildActivity(AppCompatActivity::class.java)
            .get() as Context

        preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        dao = TokenDaoImpl(context, serializer, deserializer)
    }

    @Test
    fun `add should save data into preferences`() {
        // arrange
        every { serializer.serialize(TOKEN_FOR_IMAGE) }.returns(SERIALIZED_IMAGE_TOKEN)
        assertThat(preferences.getStringSet(KEY_TOKENS_DATA, null)).isNull()

        // act
        dao.add(TOKEN_FOR_IMAGE)

        // assert
        assertThat(preferences.getStringSet(KEY_TOKENS_DATA, null))
            .isEqualTo(setOf(SERIALIZED_IMAGE_TOKEN))
    }

    @Test
    fun `getAll should read data from preferences`() {
        // arrange
        every { serializer.serialize(TOKEN_FOR_IMAGE) }.returns(SERIALIZED_IMAGE_TOKEN)
        every { serializer.serialize(TOKEN_FOR_DIRECTORY) }.returns(SERIALIZED_DIRECTORY_TOKEN)
        every { deserializer.deserialize(SERIALIZED_IMAGE_TOKEN) }.returns(TOKEN_FOR_IMAGE)
        every { deserializer.deserialize(SERIALIZED_DIRECTORY_TOKEN) }.returns(TOKEN_FOR_DIRECTORY)

        dao.add(TOKEN_FOR_IMAGE)
        dao.add(TOKEN_FOR_DIRECTORY)

        assertThat(preferences.getStringSet(KEY_TOKENS_DATA, null))
            .isEqualTo(setOf(SERIALIZED_IMAGE_TOKEN, SERIALIZED_DIRECTORY_TOKEN))

        // act
        val data = dao.getAll()

        // assert
        assertThat(data).isEqualTo(listOf(TOKEN_FOR_IMAGE, TOKEN_FOR_DIRECTORY))
    }

    @Test
    fun `removeAll should remove all data from preferences`() {
        // arrange
        every { serializer.serialize(TOKEN_FOR_IMAGE) }.returns(SERIALIZED_IMAGE_TOKEN)
        every { serializer.serialize(TOKEN_FOR_DIRECTORY) }.returns(SERIALIZED_DIRECTORY_TOKEN)

        dao.add(TOKEN_FOR_IMAGE)
        dao.add(TOKEN_FOR_DIRECTORY)

        assertThat(preferences.getStringSet(KEY_TOKENS_DATA, null))
            .isEqualTo(setOf(SERIALIZED_IMAGE_TOKEN, SERIALIZED_DIRECTORY_TOKEN))

        // act
        dao.removeAll()

        // assert
        assertThat(preferences.getStringSet(KEY_TOKENS_DATA, null))
            .isEqualTo(emptySet<String>())
    }

    @Test
    fun `remove should remove token from preferences`() {
        // arrange
        every { serializer.serialize(TOKEN_FOR_IMAGE) }.returns(SERIALIZED_IMAGE_TOKEN)
        every { serializer.serialize(TOKEN_FOR_DIRECTORY) }.returns(SERIALIZED_DIRECTORY_TOKEN)
        every { deserializer.deserialize(SERIALIZED_IMAGE_TOKEN) }.returns(TOKEN_FOR_IMAGE)
        every { deserializer.deserialize(SERIALIZED_DIRECTORY_TOKEN) }.returns(TOKEN_FOR_DIRECTORY)

        dao.add(TOKEN_FOR_IMAGE)
        dao.add(TOKEN_FOR_DIRECTORY)

        assertThat(preferences.getStringSet(KEY_TOKENS_DATA, null))
            .isEqualTo(setOf(SERIALIZED_IMAGE_TOKEN, SERIALIZED_DIRECTORY_TOKEN))

        // act
        dao.remove(TOKEN_FOR_IMAGE.authToken)

        // assert
        assertThat(preferences.getStringSet(KEY_TOKENS_DATA, null))
            .isEqualTo(setOf(SERIALIZED_DIRECTORY_TOKEN))
    }

    companion object {
        private const val SERIALIZED_IMAGE_TOKEN = "serialized-image-token"
        private const val SERIALIZED_DIRECTORY_TOKEN = "serialized-directory-token"
    }
}