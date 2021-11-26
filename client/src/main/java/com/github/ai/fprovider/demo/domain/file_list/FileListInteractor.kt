package com.github.ai.fprovider.demo.domain.file_list

import android.net.Uri
import com.github.ai.fprovider.demo.data.FileSystem
import com.github.ai.fprovider.demo.data.entity.FileEntity
import com.github.ai.fprovider.demo.data.entity.FilePath
import com.github.ai.fprovider.demo.data.entity.Result
import com.github.ai.fprovider.demo.domain.proxy_provider.ProxyProviderInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileListInteractor(
    private val fileSystem: FileSystem,
    private val proxyInteractor: ProxyProviderInteractor
) {

    suspend fun getFileList(path: FilePath): Result<List<FileEntity>> =
        withContext(Dispatchers.IO) {
            fileSystem.getFileList(path)
        }

    suspend fun getFile(path: FilePath): Result<FileEntity> =
        withContext(Dispatchers.IO) {
            fileSystem.getFile(path)
        }

    suspend fun createProxyUri(file: FileEntity): Result<Uri> =
        withContext(Dispatchers.IO) {
            Result.Success(proxyInteractor.createProxyUri(file))
        }
}