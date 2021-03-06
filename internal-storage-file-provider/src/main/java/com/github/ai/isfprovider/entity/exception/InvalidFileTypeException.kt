package com.github.ai.isfprovider.entity.exception

class InvalidFileTypeException(
    message: String
) : InternalStorageFileProviderException(message) {

    companion object {
        const val FILE_IS_NOT_A_DIRECTORY = "File %s is not a directory"
    }
}