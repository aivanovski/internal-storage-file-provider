package com.github.ai.fprovider.demo.presentation.file_list.cells

import com.github.ai.fprovider.demo.R
import com.github.ai.fprovider.demo.data.entity.FileEntity
import com.github.ai.fprovider.demo.domain.ResourceProvider
import com.github.ai.fprovider.demo.extension.isHiddenFile
import com.github.ai.fprovider.demo.extension.toPath
import com.github.ai.fprovider.demo.extension.toUri
import com.github.ai.fprovider.demo.presentation.file_list.cells.model.FileCellModel
import com.github.ai.fprovider.demo.utils.MimeTypes
import com.github.ai.fprovider.demo.utils.StringUtils

class FileListCellFactory(
    private val resourceProvider: ResourceProvider
) {

    fun createCellModels(
        parent: FileEntity?,
        files: List<FileEntity>,
        accessToken: String,
        isShowHiddenFiles: Boolean,
        onFileClicked: (file: FileEntity) -> Unit,
        onFileLongClicked: (file: FileEntity) -> Unit
    ): List<FileCellModel> {
        val allFiles = files.toMutableList().apply {
            if (parent != null) {
                add(0, parent)
            }
        }

        val idToFileMap = allFiles.associateBy { it.path }

        val onItemClick = { id: String ->
            val file = idToFileMap[id]
            if (file != null) {
                onFileClicked.invoke(file)
            }
        }

        val onItemLongClick = { id: String ->
            val file = idToFileMap[id]
            if (file != null) {
                onFileLongClicked.invoke(file)
            }
        }


        return allFiles
            .filter { file ->
                !file.isHiddenFile() || isShowHiddenFiles
            }
            .map { file ->
                val icon = when {
                    file.isDirectory -> R.drawable.ic_folder_white_24dp
                    else -> R.drawable.ic_file_white_24dp
                }

                val name = when {
                    file == parent -> ".."
                    file.isDirectory -> file.name + "/"
                    else -> file.name
                }

                val description = when {
                    file == parent -> resourceProvider.getString(R.string.parent_folder)
                    file.isDirectory -> resourceProvider.getString(R.string.folder)
                    file.size != null -> StringUtils.formatFileSize(file.size)
                    else -> "-"
                }

                val uri = if (MimeTypes.IMAGE_TYPES.contains(file.mimeType)) {
                    file.toPath(accessToken).toUri()
                } else {
                    null
                }

                FileCellModel(
                    id = file.path,
                    name = name,
                    description = description,
                    iconResId = icon,
                    imageUri = uri,
                    onClick = onItemClick,
                    onLongClick = onItemLongClick
                )
            }
    }
}