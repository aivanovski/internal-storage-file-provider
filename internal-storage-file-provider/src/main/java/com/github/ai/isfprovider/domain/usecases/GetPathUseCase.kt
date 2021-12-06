package com.github.ai.isfprovider.domain.usecases

import com.github.ai.isfprovider.data.FileSystem
import com.github.ai.isfprovider.entity.Result

internal class GetPathUseCase(
    private val fileSystem: FileSystem
) {

    fun getRealPath(path: String): Result<String> {
        return fileSystem.getRealPath(path)
    }
}