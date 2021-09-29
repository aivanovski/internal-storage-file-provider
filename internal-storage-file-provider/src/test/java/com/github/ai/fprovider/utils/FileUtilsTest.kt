package com.github.ai.fprovider.utils

import com.github.ai.fprovider.utils.Constants.ROOT
import com.github.ai.fprovider.utils.FileUtils.concatenatePath
import com.github.ai.fprovider.utils.FileUtils.getExtensionFromName
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
        assertThat(concatenatePath(ROOT_PATH, DIRECTORY_PATH))
            .isEqualTo("$ROOT_PATH/$DIRECTORY_PATH")
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

        assertThat(concatenatePath(ROOT_PATH, "/$DIRECTORY_PATH"))
            .isEqualTo("$ROOT_PATH/$DIRECTORY_PATH")

        assertThat(concatenatePath(ROOT_PATH, "/$DIRECTORY_PATH/"))
            .isEqualTo("$ROOT_PATH/$DIRECTORY_PATH")
    }

    @Test
    fun `getExtensionFromName should return extension`() {
        assertThat(getExtensionFromName(IMAGE))
            .isEqualTo(IMAGE_EXTENSION)
    }

    @Test
    fun `getExtensionFromName should return null`() {
        assertThat(getExtensionFromName("$DIRECTORY_NAME."))
            .isNull()

        assertThat(getExtensionFromName(DIRECTORY_NAME))
            .isNull()
    }

    companion object {
        private const val ROOT_PATH = "/data/data/packagename"
        private const val DIRECTORY_PATH = "home/image"
        private const val IMAGE = "image.jpg"
        private const val IMAGE_EXTENSION = "jpg"
        private const val DIRECTORY_NAME = "home"
    }
}