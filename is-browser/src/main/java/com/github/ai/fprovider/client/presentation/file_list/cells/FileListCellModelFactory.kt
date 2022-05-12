package com.github.ai.fprovider.client.presentation.file_list.cells

import com.github.ai.fprovider.client.R
import com.github.ai.fprovider.client.data.entity.FileEntity
import com.github.ai.fprovider.client.domain.ResourceProvider
import com.github.ai.fprovider.client.extension.isHiddenFile
import com.github.ai.fprovider.client.extension.toFilePath
import com.github.ai.fprovider.client.extension.toUri
import com.github.ai.fprovider.client.presentation.core.cells.BaseCellModel
import com.github.ai.fprovider.client.presentation.file_list.cells.model.FileCellModel
import com.github.ai.fprovider.client.presentation.file_list.cells.model.QuestionCellModel
import com.github.ai.fprovider.client.utils.MimeTypes
import com.github.ai.fprovider.client.utils.StringUtils

class FileListCellModelFactory(
    private val resourceProvider: ResourceProvider
) {

    fun createCellModels(
        parent: FileEntity?,
        files: List<FileEntity>,
        accessToken: String,
        isShowHiddenFiles: Boolean,
        isShowSaveDataQuestion: Boolean,
        onSaveDataCancelled: () -> Unit,
        onSaveDataConfirmed: () -> Unit,
        onFileClicked: (file: FileEntity) -> Unit,
        onFileLongClicked: (file: FileEntity) -> Unit
    ): List<BaseCellModel> {
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

        val items = mutableListOf<BaseCellModel>()

        if (isShowSaveDataQuestion) {
            items.add(
                QuestionCellModel(
                    message = resourceProvider.getString(R.string.save_data_question),
                    positiveText = resourceProvider.getString(R.string.yes),
                    negativeText = resourceProvider.getString(R.string.no),
                    onNegativeClicked = { onSaveDataCancelled.invoke() },
                    onPositiveClicked = { onSaveDataConfirmed.invoke() }
                )
            )
        }

        val visibleFiles = allFiles
            .filter { file ->
                !file.isHiddenFile() || isShowHiddenFiles
            }

        for (file in visibleFiles) {
            items.add(
                file.toFileCellModel(
                    parent = parent,
                    accessToken = accessToken,
                    onItemClick = onItemClick,
                    onItemLongClick = onItemLongClick
                )
            )
        }

        return items
    }

    private fun FileEntity.toFileCellModel(
        parent: FileEntity?,
        accessToken: String,
        onItemClick: (id: String) -> Unit,
        onItemLongClick: (id: String) -> Unit
    ): BaseCellModel {
        val icon = when {
            isDirectory -> R.drawable.ic_folder_white_24dp
            else -> R.drawable.ic_file_white_24dp
        }

        val name = when {
            this == parent -> ".."
            this.isDirectory -> "$name/"
            else -> name
        }

        val description = when {
            this == parent -> resourceProvider.getString(R.string.parent_folder)
            isDirectory -> resourceProvider.getString(R.string.folder)
            size != null -> StringUtils.formatFileSize(size)
            else -> "-"
        }

        val uri = if (MimeTypes.IMAGE_TYPES.contains(mimeType)) {
            toFilePath(accessToken).toUri()
        } else {
            null
        }

        return FileCellModel(
            id = path,
            name = name,
            description = description,
            iconResId = icon,
            imageUri = uri,
            onClick = onItemClick,
            onLongClick = onItemLongClick
        )
    }
}