package com.github.ai.fprovider.client.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringUtilsTest {

    @Test
    fun `formatFileSize should return valid strings`() {
        listOf(
            0L to "0 B",
            1L to "1 B",
            KIBI to "1 KB",
            MEBI to "1 MB",
            GIBI to "1 GB",
            TEBI to "1 TB"
        )
            .forEach { (size, result) ->
                assertThat(StringUtils.formatFileSize(size))
                    .isEqualTo(result)
            }
    }

    companion object {
        private const val KIBI = 1024L
        private const val MEBI = KIBI * KIBI
        private const val GIBI = MEBI * KIBI
        private const val TEBI = GIBI * KIBI
    }
}