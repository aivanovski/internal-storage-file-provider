package com.github.ai.fprovider.domain

import android.webkit.MimeTypeMap
import com.github.ai.fprovider.MimeTypes
import com.github.ai.fprovider.entity.FileModel

internal class MimeTypeProvider {

    fun getMimeType(file: FileModel): String? {
        return when {
            file.isDirectory -> MimeTypes.DIRECTORY
            else -> MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
        }
    }
}