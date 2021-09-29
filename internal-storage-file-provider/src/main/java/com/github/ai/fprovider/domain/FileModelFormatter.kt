package com.github.ai.fprovider.domain

import com.github.ai.fprovider.entity.FileModel
import com.github.ai.fprovider.entity.Projection
import com.github.ai.fprovider.entity.Projection.MIME_TYPE
import com.github.ai.fprovider.entity.Projection.NAME
import com.github.ai.fprovider.entity.Projection.SIZE
import com.github.ai.fprovider.entity.Projection.URI
import com.github.ai.fprovider.utils.Constants.CONTENT
import com.github.ai.fprovider.utils.Constants.EMPTY

internal class FileModelFormatter {

    fun format(
        file: FileModel,
        rootPath: String,
        authority: String,
        projection: List<Projection>,
        mimeTypeProvider: MimeTypeProvider
    ): List<String> {
        return projection
            .map { column ->
                when (column) {
                    URI -> "$CONTENT://$authority$rootPath${file.path}"
                    NAME -> file.name
                    SIZE -> file.size.toString()
                    MIME_TYPE -> mimeTypeProvider.getMimeType(file) ?: EMPTY
                }
            }
    }
}