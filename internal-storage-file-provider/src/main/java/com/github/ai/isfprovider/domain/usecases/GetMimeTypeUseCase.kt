package com.github.ai.isfprovider.domain.usecases

import com.github.ai.isfprovider.data.FileSystem
import com.github.ai.isfprovider.domain.MimeTypeProvider
import com.github.ai.isfprovider.entity.Result
import com.github.ai.isfprovider.utils.Constants.EMPTY

internal class GetMimeTypeUseCase(
    private val fileSystem: FileSystem,
    private val mimeTypeProvider: MimeTypeProvider
) {

    fun getMimeType(path: String): Result<String> {
        val getFileResult = fileSystem.getFile(path)
        if (getFileResult.isFailure) {
            return getFileResult.takeError()
        }

        val file = getFileResult.getOrThrow()
        val mime = mimeTypeProvider.getMimeType(file) ?: EMPTY

        return Result.Success(mime)
    }
}