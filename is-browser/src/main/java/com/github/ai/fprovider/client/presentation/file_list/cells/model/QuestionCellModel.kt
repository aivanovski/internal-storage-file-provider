package com.github.ai.fprovider.client.presentation.file_list.cells.model

import com.github.ai.fprovider.client.presentation.core.cells.BaseCellModel

data class QuestionCellModel(
    val message: String,
    val positiveText: String,
    val negativeText: String,
    val onNegativeClicked: () -> Unit,
    val onPositiveClicked: () -> Unit
) : BaseCellModel