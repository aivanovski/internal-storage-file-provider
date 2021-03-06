package com.github.ai.isfprovider.domain

import android.net.Uri
import com.github.ai.isfprovider.domain.usecases.CheckAuthTokenUseCase
import com.github.ai.isfprovider.domain.usecases.GetDirectoryListUseCase
import com.github.ai.isfprovider.domain.usecases.GetFileInfoUseCase
import com.github.ai.isfprovider.domain.usecases.GetMimeTypeUseCase
import com.github.ai.isfprovider.domain.usecases.GetPathUseCase
import com.github.ai.isfprovider.entity.QueryType.DIRECTORY_LIST
import com.github.ai.isfprovider.entity.QueryType.FILE_INFO
import com.github.ai.isfprovider.entity.Result
import com.github.ai.isfprovider.entity.Table
import com.github.ai.isfprovider.entity.exception.AuthFailedException
import com.github.ai.isfprovider.entity.exception.InvalidPathException
import com.github.ai.isfprovider.entity.exception.InvalidQueryTypeException

internal class Interactor(
    private val uriParser: UriParser,
    private val projectionMapper: ProjectionMapper,
    private val checkTokenUseCase: CheckAuthTokenUseCase,
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

        if (!checkTokenUseCase.isPathAccessible(data.authToken, data.path)) {
            return Result.Failure(AuthFailedException())
        }

        return mimeTypeUseCase.getMimeType(data.path)
    }

    fun getRealFilePath(uri: Uri): Result<String> {
        val data = uriParser.parse(uri)
            ?: return Result.Failure(InvalidPathException())

        if (data.queryType != FILE_INFO) {
            return Result.Failure(InvalidQueryTypeException())
        }

        if (!checkTokenUseCase.isPathAccessible(data.authToken, data.path)) {
            return Result.Failure(AuthFailedException())
        }

        return pathUseCase.getRealPath(data.path)
    }

    fun query(uri: Uri, columns: List<String>): Result<Table> {
        val data = uriParser.parse(uri)
            ?: return Result.Failure(InvalidPathException())

        if (!checkTokenUseCase.isPathAccessible(data.authToken, data.path)) {
            return Result.Failure(AuthFailedException())
        }

        val projection = projectionMapper.getProjectionFromColumns(columns)

        return when (data.queryType) {
            FILE_INFO -> fileInfoUseCase.getFileInto(data.path, projection)
            DIRECTORY_LIST -> directoryListUseCase.getDirectoryList(data.path, projection)
        }
    }
}