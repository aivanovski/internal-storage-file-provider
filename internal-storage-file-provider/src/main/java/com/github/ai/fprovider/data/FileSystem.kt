package com.github.ai.fprovider.data

import com.github.ai.fprovider.entity.FileModel
import com.github.ai.fprovider.entity.Result

internal interface FileSystem {
    fun getRealPath(path: String): Result<String>
    fun getFile(path: String): Result<FileModel>
    fun getChildFiles(parentPath: String): Result<List<FileModel>>
}