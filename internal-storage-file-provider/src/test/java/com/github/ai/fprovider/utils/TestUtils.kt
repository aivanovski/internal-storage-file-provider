package com.github.ai.fprovider.utils

import io.mockk.every
import io.mockk.mockk
import java.io.File

fun createMockedFile(
    path: String,
    name: String,
    length: Long,
    isDirectory: Boolean
): File {
    return mockk<File>()
        .also {
            every { it.path }.returns(path)
            every { it.name }.returns(name)
            every { it.length() }.returns(length)
            every { it.isDirectory }.returns(isDirectory)
        }
}
