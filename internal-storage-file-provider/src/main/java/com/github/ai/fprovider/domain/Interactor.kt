package com.github.ai.fprovider.domain

import android.net.Uri
import com.github.ai.fprovider.domain.usecases.GetDirectoryListUseCase
import com.github.ai.fprovider.domain.usecases.GetFileInfoUseCase
import com.github.ai.fprovider.domain.usecases.GetMimeTypeUseCase
import com.github.ai.fprovider.entity.QueryType.DIRECTORY_LIST
import com.github.ai.fprovider.entity.QueryType.FILE_INFO
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.entity.Table
import com.github.ai.fprovider.entity.exception.InvalidPathException

internal class Interactor(
    private val pathConverter: PathConverter,
    private val projectionMapper: ProjectionMapper,
    private val mimeTypeUseCase: GetMimeTypeUseCase,
    private val fileInfoUseCase: GetFileInfoUseCase,
    private val directoryListUseCase: GetDirectoryListUseCase
) {

    fun getMimeType(uri: Uri): Result<String> {
        val path = pathConverter.getPath(uri)
            ?: return Result.Failure(InvalidPathException())

        return mimeTypeUseCase.getMimeType(path)
    }

    fun query(uri: Uri, columns: List<String>): Result<Table> {
        val queryType = pathConverter.getQueryType(uri)
            ?: return Result.Failure(InvalidPathException())

        val path = pathConverter.getPath(uri)
            ?: return Result.Failure(InvalidPathException())

        val projection = projectionMapper.getProjectionFromColumns(columns)

        return when(queryType) {
            FILE_INFO -> fileInfoUseCase.getFileInto(path, projection)
            DIRECTORY_LIST -> directoryListUseCase.getDirectoryList(path, projection)
        }
    }
}