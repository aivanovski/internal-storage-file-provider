package com.github.ai.fprovider.utils

import com.github.ai.fprovider.test.TestData.DIRECTORY_FILE
import com.github.ai.fprovider.test.TestData.IMAGE_FILE
import com.github.ai.fprovider.test.createMockedFile
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FileExtKtTest {

    @Test
    fun `toModel should convert file`() {
        // arrange
        val file = createMockedFile(
            path = IMAGE_FILE.path,
            name = IMAGE_FILE.name,
            length = IMAGE_FILE.size,
            isDirectory = IMAGE_FILE.isDirectory
        )

        // act
        val model = file.toModel()

        // assert
        assertThat(model).isEqualTo(IMAGE_FILE)
    }

    @Test
    fun `toModel should convert directory`() {
        // arrange
        val file = createMockedFile(
            path = DIRECTORY_FILE.path,
            name = DIRECTORY_FILE.name,
            length = 12345,
            isDirectory = DIRECTORY_FILE.isDirectory
        )

        // act
        val model = file.toModel()

        // assert
        assertThat(model).isEqualTo(DIRECTORY_FILE)
    }

    @Test
    fun `toModel should convert file and trim root`() {
        // arrange
        val file = createMockedFile(
            path = "$ROOT_PATH${IMAGE_FILE.path}",
            name = IMAGE_FILE.name,
            length = IMAGE_FILE.size,
            isDirectory = IMAGE_FILE.isDirectory
        )

        // act
        val model = file.toModel(trimPathPrefix = ROOT_PATH)

        // assert
        assertThat(model).isEqualTo(IMAGE_FILE)
    }

    @Test
    fun `toModels should convert list of files`() {
        // arrange
        val file = createMockedFile(
            path = IMAGE_FILE.path,
            name = IMAGE_FILE.name,
            length = IMAGE_FILE.size,
            isDirectory = IMAGE_FILE.isDirectory
        )
        val dir = createMockedFile(
            path = DIRECTORY_FILE.path,
            name = DIRECTORY_FILE.name,
            length = DIRECTORY_FILE.size,
            isDirectory = DIRECTORY_FILE.isDirectory
        )

        // act
        val models = listOf(file, dir).toModels()

        // assert
        assertThat(models.size).isEqualTo(2)
        assertThat(models[0]).isEqualTo(IMAGE_FILE)
        assertThat(models[1]).isEqualTo(DIRECTORY_FILE)
    }

    companion object {
        private const val ROOT_PATH = "/root/home"
    }
}