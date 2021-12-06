package com.github.ai.isfprovider.domain

import android.webkit.MimeTypeMap
import com.github.ai.isfprovider.MimeTypes
import com.github.ai.isfprovider.entity.FileModel

internal class MimeTypeProvider {

    fun getMimeType(file: FileModel): String? {
        return when {
            file.isDirectory -> MimeTypes.DIRECTORY
            else -> MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
        }
    }
}