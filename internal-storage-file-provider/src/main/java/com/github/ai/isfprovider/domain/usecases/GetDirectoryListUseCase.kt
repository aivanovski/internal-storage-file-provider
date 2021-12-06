package com.github.ai.isfprovider.domain.usecases

import com.github.ai.isfprovider.data.FileSystem
import com.github.ai.isfprovider.domain.FileModelFormatter
import com.github.ai.isfprovider.domain.MimeTypeProvider
import com.github.ai.isfprovider.entity.Projection
import com.github.ai.isfprovider.entity.Result
import com.github.ai.isfprovider.entity.Table
import com.github.ai.isfprovider.utils.toColumnNames

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