package com.github.ai.isfprovider.data

import com.github.ai.isfprovider.entity.FileModel
import com.github.ai.isfprovider.entity.Result
import com.github.ai.isfprovider.entity.exception.InvalidFileTypeException
import com.github.ai.isfprovider.entity.exception.InvalidFileTypeException.Companion.FILE_IS_NOT_A_DIRECTORY
import com.github.ai.isfprovider.entity.exception.NoAccessException
import com.github.ai.isfprovider.utils.toModel
import com.github.ai.isfprovider.utils.toModels
import java.io.File
import java.io.FileNotFoundException

internal class InternalFileSystem(
    private val rootDirPath: String
) : FileSystem {

    override fun getRealPath(path: String): Result<String> {
        val file = File(rootDirPath + path)
        if (!file.exists()) {
            return Result.Failure(FileNotFoundException(file.path))
        }

        return Result.Success(file.path)
    }

    override fun getFile(path: String): Result<FileModel> {
        val file = File(rootDirPath + path)
        if (!file.exists()) {
            return Result.Failure(FileNotFoundException(file.path))
        }

        return Result.Success(file.toModel(trimPathPrefix = rootDirPath))
    }

    override fun getChildFiles(parentPath: String): Result<List<FileModel>> {
        val file = File(rootDirPath + parentPath)
        if (!file.exists()) {
            return Result.Failure(FileNotFoundException(file.path))
        }

        if (!file.isDirectory) {
            return Result.Failure(
                InvalidFileTypeException(
                    message = String.format(FILE_IS_NOT_A_DIRECTORY, file)
                )
            )
        }

        // TODO: write test for NoAccessException
        val files = file.listFiles()
            ?.toList()
            ?.toModels(trimPathPrefix = rootDirPath)
            ?: return Result.Failure(NoAccessException(file))

        return Result.Success(files)
    }
}