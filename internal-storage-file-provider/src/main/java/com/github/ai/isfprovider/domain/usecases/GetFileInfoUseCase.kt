package com.github.ai.isfprovider.domain.usecases

import com.github.ai.isfprovider.data.FileSystem
import com.github.ai.isfprovider.domain.FileModelFormatter
import com.github.ai.isfprovider.domain.MimeTypeProvider
import com.github.ai.isfprovider.entity.Projection
import com.github.ai.isfprovider.entity.Result
import com.github.ai.isfprovider.entity.Table
import com.github.ai.isfprovider.utils.toColumnNames

internal class GetFileInfoUseCase(
    private val fileSystem: FileSystem,
    private val mimeTypeProvider: MimeTypeProvider,
    private val fileModelFormatter: FileModelFormatter,
    private val authority: String
) {

    fun getFileInto(path: String, projection: List<Projection>): Result<Table> {
        val getFileResult = fileSystem.getFile(path)
        if (getFileResult.isFailure) {
            return getFileResult.takeError()
        }

        val file = getFileResult.getOrThrow()
        val row = fileModelFormatter.format(
            file = file,
            authority = authority,
            projection = projection,
            mimeTypeProvider = mimeTypeProvider
        )

        return Result.Success(
            Table(
                rows = listOf(row),
                columns = projection.toColumnNames()
            )
        )
    }
}