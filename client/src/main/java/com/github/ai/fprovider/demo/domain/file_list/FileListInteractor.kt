package com.github.ai.fprovider.demo.domain.file_list

import com.github.ai.fprovider.demo.data.FileSystem
import com.github.ai.fprovider.demo.data.entity.FileEntity
import com.github.ai.fprovider.demo.data.entity.FilePath
import com.github.ai.fprovider.demo.data.entity.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileListInteractor(
    private val fileSystem: FileSystem
) {

    suspend fun getFileList(path: FilePath): Result<List<FileEntity>> =
        withContext(Dispatchers.IO) {
            fileSystem.getFileList(path)
        }

    suspend fun getFile(path: FilePath): Result<FileEntity> =
        withContext(Dispatchers.IO) {
            fileSystem.getFile(path)
        }
}