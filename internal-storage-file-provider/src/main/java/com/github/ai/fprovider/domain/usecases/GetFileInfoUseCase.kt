package com.github.ai.fprovider.domain.usecases

import com.github.ai.fprovider.data.FileSystem
import com.github.ai.fprovider.domain.FileModelFormatter
import com.github.ai.fprovider.domain.MimeTypeProvider
import com.github.ai.fprovider.entity.Projection
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.entity.Table
import com.github.ai.fprovider.utils.toColumnNames

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