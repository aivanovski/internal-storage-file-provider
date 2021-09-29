package com.github.ai.fprovider.data

import com.github.ai.fprovider.entity.FileModel
import com.github.ai.fprovider.entity.Result

internal interface FileSystem {
    fun getFile(path: String): Result<FileModel>
}