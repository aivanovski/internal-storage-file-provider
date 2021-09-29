package com.github.ai.fprovider.domain

import android.net.Uri
import com.github.ai.fprovider.domain.usecases.GetMimeTypeUseCase
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.entity.exception.InvalidPathException

internal class Interactor(
    private val pathConverter: PathConverter,
    private val mimeTypeUseCase: GetMimeTypeUseCase
) {

    fun getMimeType(uri: Uri): Result<String> {
        val path = pathConverter.getPath(uri)
            ?: return Result.Failure(InvalidPathException())

        return mimeTypeUseCase.getMimeType(path)
    }
}