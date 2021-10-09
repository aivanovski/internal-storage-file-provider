package com.github.ai.fprovider.demo.data.exception

open class FileSystemProviderException(
    message: String? = null,
    cause: Exception? = null
) : Exception(message, cause)