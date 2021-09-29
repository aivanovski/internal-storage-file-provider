package com.github.ai.fprovider.test

import com.github.ai.fprovider.MimeTypes
import com.github.ai.fprovider.entity.FileModel

internal object TestData {

    const val IMAGE_MIME_TYPE = "image/jpeg"
    const val DIRECTORY_MIME_TYPE = MimeTypes.DIRECTORY
    const val AUTHORITY = "com.test.authority"

    val IMAGE_FILE = FileModel(
        path = "/home/image.jpg",
        name = "image.jpg",
        size = 12345L,
        isDirectory = false
    )

    val DIRECTORY_FILE = FileModel(
        path = "/home/tmp",
        name = "tmp",
        size = -1L,
        isDirectory = true
    )
}