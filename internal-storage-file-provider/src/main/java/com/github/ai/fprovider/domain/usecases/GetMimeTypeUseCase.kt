package com.github.ai.fprovider.domain.usecases

import com.github.ai.fprovider.data.FileSystem
import com.github.ai.fprovider.domain.MimeTypeProvider
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.utils.Constants.EMPTY

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