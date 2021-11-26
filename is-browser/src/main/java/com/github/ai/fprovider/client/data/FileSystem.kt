package com.github.ai.fprovider.client.data

import com.github.ai.fprovider.client.data.entity.FileEntity
import com.github.ai.fprovider.client.data.entity.FilePath
import com.github.ai.fprovider.client.data.entity.Result

interface FileSystem {
    fun getFile(path: FilePath): Result<FileEntity>
    fun getFileList(path: FilePath): Result<List<FileEntity>>
}