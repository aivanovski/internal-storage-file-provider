package com.github.ai.fprovider.test

import com.github.ai.fprovider.MimeTypes
import com.github.ai.fprovider.entity.FileModel

internal object TestData {

    const val IMAGE_MIME_TYPE = "image/jpeg"
    const val DIRECTORY_MIME_TYPE = MimeTypes.DIRECTORY
    const val AUTHORITY = "com.test.authority"

    val PARENT_FILE = FileModel(
        path = "/home",
        name = "home",
        size = -1L,
        isDirectory = true
    )

    val IMAGE_FILE = FileModel(
        path = PARENT_FILE.path + "/image.jpg",
        name = "image.jpg",
        size = 12345L,
        isDirectory = false
    )

    val DIRECTORY_FILE = FileModel(
        path = PARENT_FILE.path + "/tmp",
        name = "tmp",
        size = -1L,
        isDirectory = true
    )
}