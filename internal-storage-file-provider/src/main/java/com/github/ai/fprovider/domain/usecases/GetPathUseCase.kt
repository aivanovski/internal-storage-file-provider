package com.github.ai.fprovider.domain.usecases

import com.github.ai.fprovider.data.FileSystem
import com.github.ai.fprovider.entity.Result

internal class GetPathUseCase(
    private val fileSystem: FileSystem
) {

    fun getRealPath(path: String): Result<String> {
        return fileSystem.getRealPath(path)
    }
}