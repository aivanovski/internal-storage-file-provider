package com.github.ai.fprovider.demo.data

import com.github.ai.fprovider.demo.data.entity.FileEntity
import com.github.ai.fprovider.demo.data.entity.FilePath
import com.github.ai.fprovider.demo.data.entity.Result

interface FileSystem {
    fun getFile(path: FilePath): Result<FileEntity>
    fun getFileList(path: FilePath): Result<List<FileEntity>>
}