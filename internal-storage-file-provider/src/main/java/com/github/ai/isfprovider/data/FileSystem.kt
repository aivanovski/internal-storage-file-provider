package com.github.ai.isfprovider.data

import com.github.ai.isfprovider.entity.FileModel
import com.github.ai.isfprovider.entity.Result

internal interface FileSystem {
    fun getRealPath(path: String): Result<String>
    fun getFile(path: String): Result<FileModel>
    fun getChildFiles(parentPath: String): Result<List<FileModel>>
}