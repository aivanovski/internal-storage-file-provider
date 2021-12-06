package com.github.ai.isfprovider.domain

import com.github.ai.isfprovider.entity.FileModel
import com.github.ai.isfprovider.entity.Projection
import com.github.ai.isfprovider.entity.Projection.MIME_TYPE
import com.github.ai.isfprovider.entity.Projection.NAME
import com.github.ai.isfprovider.entity.Projection.SIZE
import com.github.ai.isfprovider.entity.Projection.URI
import com.github.ai.isfprovider.utils.Constants.CONTENT
import com.github.ai.isfprovider.utils.Constants.EMPTY

internal class FileModelFormatter {

    fun format(
        file: FileModel,
        authority: String,
        projection: List<Projection>,
        mimeTypeProvider: MimeTypeProvider
    ): List<String> {
        return projection
            .map { column ->
                when (column) {
                    URI -> "$CONTENT://$authority${file.path}"
                    NAME -> file.name
                    SIZE -> file.size.toString()
                    MIME_TYPE -> mimeTypeProvider.getMimeType(file) ?: EMPTY
                }
            }
    }
}