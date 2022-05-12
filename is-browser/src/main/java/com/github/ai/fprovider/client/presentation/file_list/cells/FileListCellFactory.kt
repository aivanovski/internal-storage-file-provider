package com.github.ai.fprovider.client.presentation.file_list.cells

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import com.github.ai.fprovider.client.presentation.core.cells.BaseCellViewModel
import com.github.ai.fprovider.client.presentation.file_list.cells.cell.FileCell
import com.github.ai.fprovider.client.presentation.file_list.cells.cell.QuestionCell
import com.github.ai.fprovider.client.presentation.file_list.cells.viewmodel.FileCellViewModel
import com.github.ai.fprovider.client.presentation.file_list.cells.viewmodel.QuestionCellViewModel

class FileListCellFactory {

    @Composable
    @ExperimentalFoundationApi
    fun createCell(viewModel: BaseCellViewModel) {
        when (viewModel) {
            is FileCellViewModel -> FileCell(viewModel)
            is QuestionCellViewModel -> QuestionCell(viewModel)
        }
    }
}