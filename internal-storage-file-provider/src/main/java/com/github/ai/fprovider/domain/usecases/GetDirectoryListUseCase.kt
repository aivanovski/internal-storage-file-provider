package com.github.ai.fprovider.domain.usecases

import com.github.ai.fprovider.data.FileSystem
import com.github.ai.fprovider.domain.FileModelFormatter
import com.github.ai.fprovider.domain.MimeTypeProvider
import com.github.ai.fprovider.entity.Projection
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.entity.Table
import com.github.ai.fprovider.utils.toColumnNames

internal class GetDirectoryListUseCase(
    private val fileSystem: FileSystem,
    private val fileModelFormatter: FileModelFormatter,
    private val mimeTypeProvider: MimeTypeProvider,
    private val authority: String
) {

    fun getDirectoryList(path: String, projection: List<Projection>): Result<Table> {
        val getFileList = fileSystem.getChildFiles(path)
        if (getFileList.isFailure) {
            return getFileList.takeError()
        }

        val files = getFileList.getOrThrow()
        val rows = files
            .map {
                fileModelFormatter.format(
                    file = it,
                    authority = authority,
                    projection = projection,
                    mimeTypeProvider = mimeTypeProvider
                )
            }

        return Result.Success(
            Table(
                rows = rows,
                columns = projection.toColumnNames()
            )
        )
    }
}