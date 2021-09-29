package com.github.ai.fprovider

import android.content.ContentResolver
import android.content.Context
import android.os.RemoteException
import androidx.appcompat.app.AppCompatActivity
import com.github.ai.fprovider.domain.Interactor
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.test.TestData.AUTHORITY
import com.github.ai.fprovider.test.TestData.DIRECTORY_FILE
import com.github.ai.fprovider.test.TestData.DIRECTORY_MIME_TYPE
import com.github.ai.fprovider.test.TestData.IMAGE_FILE
import com.github.ai.fprovider.test.TestData.IMAGE_MIME_TYPE
import com.github.ai.fprovider.test.toUri
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowContentResolver
import java.io.FileNotFoundException
import java.lang.Exception

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class InternalStorageFileProviderTest {

    private lateinit var contentResolver: ContentResolver
    private lateinit var interactor: Interactor

    @Before
    fun setUp() {
        val context = Robolectric
            .buildActivity(AppCompatActivity::class.java)
            .get() as Context

        interactor = mockk()

        ShadowContentResolver.registerProviderInternal(
            AUTHORITY,
            InternalStorageFileProvider(
                interactor = interactor
            )
        )

        contentResolver = context.contentResolver
    }

    @Test
    fun `getType should return mimeType for file`() {
        // arrange
        val uri = IMAGE_FILE.toUri()
        every { interactor.getMimeType(uri) }.returns(Result.Success(IMAGE_MIME_TYPE))

        // act
        val mimeType = contentResolver.getType(uri)

        // assert
        assertThat(mimeType).isEqualTo(IMAGE_MIME_TYPE)
    }

    @Test
    fun `getType should return mimeType for directory`() {
        // arrange
        val uri = DIRECTORY_FILE.toUri()
        every { interactor.getMimeType(uri) }.returns(Result.Success(DIRECTORY_MIME_TYPE))

        // act
        val mimeType = contentResolver.getType(uri)

        // assert
        assertThat(mimeType).isEqualTo(DIRECTORY_MIME_TYPE)
    }

    @Test
    fun `getType should throw exception`() {
        // arrange
        val expectedException = FileNotFoundException()
        val uri = DIRECTORY_FILE.toUri()
        every { interactor.getMimeType(uri) }.returns(Result.Failure(expectedException))

        // act
        val exception = catchException { contentResolver.getType(uri) }

        // arrange
        assertThat(exception).isInstanceOf(RemoteException::class.java)
        assertThat(exception.message).isEqualTo(expectedException.toString())
    }

    @Test(expected = RuntimeException::class)
    fun query() {
        // TODO: to be done
        contentResolver.query(IMAGE_FILE.toUri(), null, null, null, null)
    }

    @Test(expected = RuntimeException::class)
    fun insert() {
        // TODO: to be done
        contentResolver.insert(IMAGE_FILE.toUri(), null)
    }

    @Test(expected = RuntimeException::class)
    fun delete() {
        // TODO: to be done
        contentResolver.delete(IMAGE_FILE.toUri(), null, null)
    }

    @Test(expected = RuntimeException::class)
    fun update() {
        // TODO: to be done
        contentResolver.update(IMAGE_FILE.toUri(), null, null, null)
    }

    private fun catchException(block: () -> Unit): Exception {
        var error: Exception? = null

        try {
            block.invoke()
        } catch (e: Exception) {
            error = e

        }

        return error ?: throw IllegalStateException("Unable to catch exception")
    }
}