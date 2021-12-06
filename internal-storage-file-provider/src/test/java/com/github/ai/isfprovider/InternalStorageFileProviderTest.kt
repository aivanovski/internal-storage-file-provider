package com.github.ai.isfprovider

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.RemoteException
import androidx.appcompat.app.AppCompatActivity
import com.github.ai.isfprovider.domain.Interactor
import com.github.ai.isfprovider.entity.Projection
import com.github.ai.isfprovider.entity.Result
import com.github.ai.isfprovider.entity.Table
import com.github.ai.isfprovider.test.TestData.AUTHORITY
import com.github.ai.isfprovider.test.TestData.DIRECTORY_FILE
import com.github.ai.isfprovider.test.TestData.DIRECTORY_MIME_TYPE
import com.github.ai.isfprovider.test.TestData.IMAGE_FILE
import com.github.ai.isfprovider.test.TestData.IMAGE_MIME_TYPE
import com.github.ai.isfprovider.test.toUri
import com.github.ai.isfprovider.utils.toColumnNames
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

    @Test
    fun `query should return file info`() {
        // arrange
        val uri = IMAGE_FILE.toUri()
        every { interactor.query(uri, ALL_COLUMNS) }.returns(Result.Success(IMAGE_FILE_INFO))

        // act
        val cursor = contentResolver.query(
            uri,
            ALL_COLUMNS.toTypedArray(),
            null,
            null,
            null
        )

        // assert
        assertThat(cursor).isNotNull()
        if (cursor != null) {
            assertThat(cursor.count).isEqualTo(IMAGE_FILE_INFO.rows.size)
            assertThat(cursor.columnNames).isEqualTo(IMAGE_FILE_INFO.columns.toTypedArray())

            val info = IMAGE_FILE_INFO.rows.first()
            cursor.moveToFirst()
            assertThat(cursor.getString(0)).isEqualTo(info[0])
            assertThat(cursor.getString(1)).isEqualTo(info[1])
            assertThat(cursor.getString(2)).isEqualTo(info[2])
            assertThat(cursor.getString(3)).isEqualTo(info[3])
        }
    }

    @Test
    fun `query should return directory list`() {
        // arrange
        val uri = Uri.parse("content://$AUTHORITY/*")
        every { interactor.query(uri, ALL_COLUMNS) }.returns(Result.Success(DIRECTORY_LIST))

        // act
        val cursor = contentResolver.query(
            uri,
            ALL_COLUMNS.toTypedArray(),
            null,
            null,
            null
        )

        // assert
        assertThat(cursor).isNotNull()
        if (cursor != null) {
            assertThat(cursor.count).isEqualTo(DIRECTORY_LIST.rows.size)
            assertThat(cursor.columnNames).isEqualTo(DIRECTORY_LIST.columns.toTypedArray())

            val firstExpectedFile = DIRECTORY_LIST.rows[0]
            cursor.moveToNext()
            assertThat(cursor.getString(0)).isEqualTo(firstExpectedFile[0])
            assertThat(cursor.getString(1)).isEqualTo(firstExpectedFile[1])
            assertThat(cursor.getString(2)).isEqualTo(firstExpectedFile[2])
            assertThat(cursor.getString(3)).isEqualTo(firstExpectedFile[3])

            val secondExpectedFile = DIRECTORY_LIST.rows[1]
            cursor.moveToNext()
            assertThat(cursor.getString(0)).isEqualTo(secondExpectedFile[0])
            assertThat(cursor.getString(1)).isEqualTo(secondExpectedFile[1])
            assertThat(cursor.getString(2)).isEqualTo(secondExpectedFile[2])
            assertThat(cursor.getString(3)).isEqualTo(secondExpectedFile[3])
        }
    }

    @Test
    fun `query should throw exception`() {
        // arrange
        val expectedException = FileNotFoundException()
        val uri = IMAGE_FILE.toUri()
        every { interactor.query(uri, ALL_COLUMNS) }.returns(Result.Failure(expectedException))

        // act
        val exception = catchException {
            contentResolver.query(
                IMAGE_FILE.toUri(),
                ALL_COLUMNS.toTypedArray(),
                null,
                null,
                null
            )
        }

        // assert
        assertThat(exception).isInstanceOf(RemoteException::class.java)
        assertThat(exception.message).isEqualTo(expectedException.toString())
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

    companion object {
        private val PROJECTION = Projection.values().toList()
        private val ALL_COLUMNS = PROJECTION.toColumnNames()

        private val IMAGE_FILE_INFO = Table(
            rows = listOf(
                ALL_COLUMNS.map { "$it-placeholder" }
            ),
            columns = ALL_COLUMNS
        )
        private val DIRECTORY_LIST = Table(
            rows = listOf(
                ALL_COLUMNS.map { "$it-file-placeholder" },
                ALL_COLUMNS.map { "$it-dir-placeholder" },
            ),
            columns = ALL_COLUMNS
        )
    }
}