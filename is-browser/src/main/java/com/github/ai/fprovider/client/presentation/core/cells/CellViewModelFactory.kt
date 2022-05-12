package com.github.ai.fprovider.client.presentation.core.cells

abstract class CellViewModelFactory {

    abstract fun crateViewModel(model: BaseCellModel): BaseCellViewModel

    fun createViewModels(models: List<BaseCellModel>): List<BaseCellViewModel> {
        return models.map { crateViewModel(it) }
    }

    protected fun throwUnsupportedModelType(model: BaseCellModel): Nothing {
        throw IllegalArgumentException("Unsupported model type: ${model.javaClass}")
    }
}