package com.github.ai.fprovider.data

import com.github.ai.fprovider.entity.FileModel
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.utils.toModel
import java.io.File
import java.io.FileNotFoundException

internal class InternalFileSystem(
    private val rootDirPath: String
) : FileSystem {

    override fun getFile(path: String): Result<FileModel> {
        val file = File(rootDirPath + path)
        if (!file.exists()) {
            return Result.Failure(FileNotFoundException(file.path))
        }

        return Result.Success(file.toModel(trimPathPrefix = rootDirPath))
    }
}