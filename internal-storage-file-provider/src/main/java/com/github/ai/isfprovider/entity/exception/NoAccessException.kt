package com.github.ai.isfprovider.entity.exception

import java.io.File

class NoAccessException(
    file: File
) : InternalStorageFileProviderException(String.format(NO_ACCESS_TO_FILE, file.path)) {

    companion object {
        private const val NO_ACCESS_TO_FILE = "No acess to file %s"
    }
}