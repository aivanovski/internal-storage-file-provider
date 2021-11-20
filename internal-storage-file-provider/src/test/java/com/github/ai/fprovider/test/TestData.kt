package com.github.ai.fprovider.test

import com.github.ai.fprovider.MimeTypes
import com.github.ai.fprovider.entity.FileModel
import com.github.ai.fprovider.entity.TokenAndPath

internal object TestData {

    const val IMAGE_MIME_TYPE = "image/jpeg"
    const val DIRECTORY_MIME_TYPE = MimeTypes.DIRECTORY
    const val AUTHORITY = "com.test.authority"
    const val AUTH_TOKEN = "auth_token"
    const val VALID_TOKEN = "valid_auth-token-1234567890"
    const val INVALID_TOKEN = "abc"

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

    val TOKEN_FOR_IMAGE = TokenAndPath(
        authToken = "valid-image-token",
        rootPath = IMAGE_FILE.path
    )

    val TOKEN_FOR_DIRECTORY = TokenAndPath(
        authToken = "valid-dir-token",
        rootPath = DIRECTORY_FILE.path
    )
}