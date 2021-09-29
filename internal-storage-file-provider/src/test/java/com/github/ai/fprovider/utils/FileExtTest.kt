package com.github.ai.fprovider.utils

import com.github.ai.fprovider.test.createMockedFile
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FileExtTest {

    @Test
    fun `toModel should convert file`() {
        // arrange
        val file = createMockedFile(
            path = FIRST_PATH,
            name = FIRST_NAME,
            length = FIRST_LENGTH,
            isDirectory = FIRST_IS_DIRECTORY
        )

        // act
        val model = file.toModel()

        // assert
        assertThat(model.path).isEqualTo(FIRST_PATH)
        assertThat(model.name).isEqualTo(FIRST_NAME)
        assertThat(model.extension).isEqualTo(FIRST_EXTENSION)
        assertThat(model.size).isEqualTo(FIRST_LENGTH)
        assertThat(model.isDirectory).isEqualTo(FIRST_IS_DIRECTORY)
    }

    @Test
    fun `toModel should convert file and trim root`() {
        // arrange
        val file = createMockedFile(
            path = "$ROOT_PATH$FIRST_PATH",
            name = FIRST_NAME,
            length = FIRST_LENGTH,
            isDirectory = FIRST_IS_DIRECTORY
        )

        // act
        val model = file.toModel(trimPathPrefix = ROOT_PATH)

        // assert
        assertThat(model.path).isEqualTo(FIRST_PATH)
        assertThat(model.name).isEqualTo(FIRST_NAME)
        assertThat(model.extension).isEqualTo(FIRST_EXTENSION)
        assertThat(model.size).isEqualTo(FIRST_LENGTH)
        assertThat(model.isDirectory).isEqualTo(FIRST_IS_DIRECTORY)
    }

    @Test
    fun `toModels should convert list of files`() {
        // arrange
        val file = createMockedFile(
            path = FIRST_PATH,
            name = FIRST_NAME,
            length = FIRST_LENGTH,
            isDirectory = FIRST_IS_DIRECTORY
        )
        val dir = createMockedFile(
            path = SECOND_PATH,
            name = SECOND_NAME,
            length = SECOND_LENGTH,
            isDirectory = SECOND_IS_DIRECTORY
        )

        // act
        val models = listOf(file, dir).toModels()

        // assert
        assertThat(models.size).isEqualTo(2)
        val fileModel = models[0]
        assertThat(fileModel.path).isEqualTo(FIRST_PATH)
        assertThat(fileModel.name).isEqualTo(FIRST_NAME)
        assertThat(fileModel.extension).isEqualTo(FIRST_EXTENSION)
        assertThat(fileModel.size).isEqualTo(FIRST_LENGTH)
        assertThat(fileModel.isDirectory).isEqualTo(FIRST_IS_DIRECTORY)

        val dirModel = models[1]
        assertThat(dirModel.path).isEqualTo(SECOND_PATH)
        assertThat(dirModel.name).isEqualTo(SECOND_NAME)
        assertThat(dirModel.extension).isNull()
        assertThat(dirModel.size).isEqualTo(SECOND_LENGTH)
        assertThat(dirModel.isDirectory).isEqualTo(SECOND_IS_DIRECTORY)
    }

    companion object {
        private const val ROOT_PATH = "/root/home"

        private const val FIRST_PATH = "/path/name.ext"
        private const val FIRST_NAME = "name.ext"
        private const val FIRST_EXTENSION = "ext"
        private const val FIRST_LENGTH = 12345L
        private const val FIRST_IS_DIRECTORY = false

        private const val SECOND_PATH = "/path/dir"
        private const val SECOND_NAME = "dir"
        private const val SECOND_LENGTH = -1L
        private const val SECOND_IS_DIRECTORY = true
    }
}