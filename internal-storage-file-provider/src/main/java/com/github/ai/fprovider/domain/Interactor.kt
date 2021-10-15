package com.github.ai.fprovider.domain

import android.net.Uri
import com.github.ai.fprovider.domain.usecases.GetDirectoryListUseCase
import com.github.ai.fprovider.domain.usecases.GetFileInfoUseCase
import com.github.ai.fprovider.domain.usecases.GetMimeTypeUseCase
import com.github.ai.fprovider.domain.usecases.GetPathUseCase
import com.github.ai.fprovider.entity.QueryType.DIRECTORY_LIST
import com.github.ai.fprovider.entity.QueryType.FILE_INFO
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.entity.Table
import com.github.ai.fprovider.entity.exception.InvalidPathException
import com.github.ai.fprovider.entity.exception.InvalidQueryTypeException

internal class Interactor(
    private val uriParser: UriParser,
    private val projectionMapper: ProjectionMapper,
    private val mimeTypeUseCase: GetMimeTypeUseCase,
    private val fileInfoUseCase: GetFileInfoUseCase,
    private val directoryListUseCase: GetDirectoryListUseCase,
    private val pathUseCase: GetPathUseCase
) {

    fun getMimeType(uri: Uri): Result<String> {
        val data = uriParser.parse(uri)
            ?: return Result.Failure(InvalidPathException())

        if (data.queryType != FILE_INFO) {
            return Result.Failure(InvalidQueryTypeException())
        }

        return mimeTypeUseCase.getMimeType(data.path)
    }

    fun getRealFilePath(uri: Uri): Result<String> {
        val data = uriParser.parse(uri)
            ?: return Result.Failure(InvalidPathException())

        if (data.queryType != FILE_INFO) {
            return Result.Failure(InvalidQueryTypeException())
        }

        return pathUseCase.getRealPath(data.path)
    }

    fun query(uri: Uri, columns: List<String>): Result<Table> {
        val data = uriParser.parse(uri)
            ?: return Result.Failure(InvalidPathException())

        val projection = projectionMapper.getProjectionFromColumns(columns)

        return when(data.queryType) {
            FILE_INFO -> fileInfoUseCase.getFileInto(data.path, projection)
            DIRECTORY_LIST -> directoryListUseCase.getDirectoryList(data.path, projection)
        }
    }
}