package com.github.ai.fprovider.demo.data.exception

import android.net.Uri

class FileNotFoundException(uri: Uri) : FileSystemProviderException(
    message = String.format("Failed to find file: %s", uri)
)