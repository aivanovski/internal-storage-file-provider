package com.github.ai.fprovider.client.presentation.file_list.cells

import com.github.ai.fprovider.client.presentation.core.cells.BaseCellModel
import com.github.ai.fprovider.client.presentation.core.cells.BaseCellViewModel
import com.github.ai.fprovider.client.presentation.core.cells.CellViewModelFactory
import com.github.ai.fprovider.client.presentation.file_list.cells.model.FileCellModel
import com.github.ai.fprovider.client.presentation.file_list.cells.model.QuestionCellModel
import com.github.ai.fprovider.client.presentation.file_list.cells.viewmodel.FileCellViewModel
import com.github.ai.fprovider.client.presentation.file_list.cells.viewmodel.QuestionCellViewModel

class FileListCellViewModelFactory : CellViewModelFactory() {

    override fun crateViewModel(model: BaseCellModel): BaseCellViewModel {
        return when (model) {
            is FileCellModel -> FileCellViewModel(model)
            is QuestionCellModel -> QuestionCellViewModel(model)
            else -> throwUnsupportedModelType(model)
        }
    }
}