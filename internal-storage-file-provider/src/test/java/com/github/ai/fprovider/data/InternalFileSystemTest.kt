package com.github.ai.fprovider.data

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.github.ai.fprovider.entity.exception.InvalidFileTypeException
import com.github.ai.fprovider.test.TestData.DIRECTORY_FILE
import com.github.ai.fprovider.test.TestData.IMAGE_FILE
import com.github.ai.fprovider.test.TestData.PARENT_FILE
import com.github.ai.fprovider.test.createWithContent
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.io.FileNotFoundException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class InternalFileSystemTest {

    private lateinit var rootDir: File
    private lateinit var fileSystem: InternalFileSystem

    @Before
    fun setUp() {
        val context = Robolectric
            .buildActivity(AppCompatActivity::class.java)
            .get() as Context

        rootDir = context.filesDir ?: throw IllegalStateException()
        fileSystem = InternalFileSystem(
            rootDirPath = rootDir.path
        )
    }

    @Test
    fun `getFile should return file`() {
        // arrange
        val file = File(rootDir.path + IMAGE_FILE.path)
        assertThat(file.exists()).isFalse()
        file.createWithContent(FILE_CONTENT)

        // act
        val result = fileSystem.getFile(IMAGE_FILE.path)

        // assert
        assertThat(result.isSuccess).isTrue()

        val model = result.getOrThrow()
        assertThat(model.path).isEqualTo(IMAGE_FILE.path)
        assertThat(model.name).isEqualTo(IMAGE_FILE.name)
        assertThat(model.isDirectory).isEqualTo(IMAGE_FILE.isDirectory)
        assertThat(model.size).isEqualTo(FILE_CONTENT.length)
    }

    @Test
    fun `getFile should return FileNotFoundException`() {
        // arrange
        val file = File(rootDir.path + IMAGE_FILE.path)
        assertThat(file.exists()).isFalse()

        // act
        val result = fileSystem.getFile(IMAGE_FILE.path)

        // assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.getExceptionOrThrow()).isInstanceOf(FileNotFoundException::class.java)
    }

    @Test
    fun `getChildFiles should return list of files`() {
        // arrange
        val dir = File(rootDir.path + PARENT_FILE.path)
        dir.mkdirs()
        assertThat(dir.exists()).isTrue()

        val innerFile = File(rootDir.path + IMAGE_FILE.path)
        innerFile.createWithContent(FILE_CONTENT)

        val innerDir = File(rootDir.path + DIRECTORY_FILE.path)
        innerDir.mkdirs()
        assertThat(innerFile.exists()).isTrue()
        assertThat(innerDir.exists()).isTrue()

        // act
        val result = fileSystem.getChildFiles(PARENT_FILE.path)

        // assert
        assertThat(result.isSuccess).isTrue()

        val models = result.getOrThrow()
        assertThat(models.size).isEqualTo(2)

        val imageFileModel = models.first { it.name == IMAGE_FILE.name }
        assertThat(imageFileModel).isEqualTo(
            IMAGE_FILE.copy(
                size = FILE_CONTENT.length.toLong()
            )
        )

        val dirFileModel = models.first { it.name == DIRECTORY_FILE.name }
        assertThat(dirFileModel).isEqualTo(DIRECTORY_FILE)
    }

    @Test
    fun `getChildFiles should return FileNotFoundException`() {
        // arrange
        val dir = File(rootDir.path + DIRECTORY_FILE.path)
        assertThat(dir.exists()).isFalse()

        // act
        val model = fileSystem.getChildFiles(DIRECTORY_FILE.path)

        // assert
        assertThat(model.isFailure).isTrue()
        assertThat(model.getExceptionOrThrow()).isInstanceOf(FileNotFoundException::class.java)
    }

    @Test
    fun `getChildFiles should return InvalidFileTypeException`() {
        // arrange
        val file = File(rootDir.path + IMAGE_FILE.path)
        file.createWithContent(FILE_CONTENT)
        assertThat(file.exists()).isTrue()

        // act
        val model = fileSystem.getChildFiles(IMAGE_FILE.path)

        // assert
        assertThat(model.isFailure).isTrue()
        assertThat(model.getExceptionOrThrow()).isInstanceOf(InvalidFileTypeException::class.java)
    }

    companion object {
        private const val FILE_CONTENT = "abcd1234"
    }
}