package com.github.ai.fprovider.utils

import com.github.ai.fprovider.utils.Constants.ROOT
import com.github.ai.fprovider.utils.FileUtils.concatenatePath
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FileUtilsTest {

    @Test
    fun `concatenatePath should work with file`() {
        assertThat(concatenatePath(ROOT_PATH, IMAGE))
            .isEqualTo("$ROOT_PATH/$IMAGE")
    }

    @Test
    fun `concatenatePath should work with directory`() {
        assertThat(concatenatePath(ROOT_PATH, DIRECTORY))
            .isEqualTo("$ROOT_PATH/$DIRECTORY")
    }

    @Test
    fun `concatenatePath should work with root`() {
        assertThat(concatenatePath(ROOT_PATH, ROOT))
            .isEqualTo(ROOT_PATH)
    }

    @Test
    fun `concatenatePath should work with additional separators`() {
        assertThat(concatenatePath(ROOT_PATH, "/$IMAGE"))
            .isEqualTo("$ROOT_PATH/$IMAGE")

        assertThat(concatenatePath(ROOT_PATH, "/$DIRECTORY"))
            .isEqualTo("$ROOT_PATH/$DIRECTORY")

        assertThat(concatenatePath(ROOT_PATH, "/$DIRECTORY/"))
            .isEqualTo("$ROOT_PATH/$DIRECTORY")
    }

    companion object {
        private const val ROOT_PATH = "/data/data/packagename"
        private const val IMAGE = "image.jpg"
        private const val DIRECTORY = "home/image"
    }
}