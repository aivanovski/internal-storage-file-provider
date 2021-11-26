package com.github.ai.fprovider.client.data.exception

open class FileSystemProviderException(
    message: String? = null,
    cause: Exception? = null
) : Exception(message, cause)