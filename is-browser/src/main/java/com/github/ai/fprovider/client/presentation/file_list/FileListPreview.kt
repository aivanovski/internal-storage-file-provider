package com.github.ai.fprovider.client.presentation.file_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.ai.fprovider.client.presentation.core.model.ScreenState
import com.github.ai.fprovider.client.presentation.file_list.cells.FileListCellFactory
import com.github.ai.fprovider.client.presentation.file_list.cells.cell.DIRECTORY_MODEL
import com.github.ai.fprovider.client.presentation.file_list.cells.cell.FILE_MODEL
import com.github.ai.fprovider.client.presentation.file_list.cells.viewmodel.FileCellViewModel
import com.github.ai.fprovider.client.presentation.theme.AppTheme
import com.github.ai.fprovider.client.presentation.theme.backgroundColor

private val CELLS = listOf(
    FileCellViewModel(DIRECTORY_MODEL),
    FileCellViewModel(FILE_MODEL)
)

@ExperimentalFoundationApi
@Composable
private fun Preview(
    state: ScreenState,
    cellViewModels: List<FileCellViewModel>
) {
    AppTheme {
        Box(modifier = Modifier.background(backgroundColor)) {
            FileListLayout(
                state = state,
                cellViewModels = cellViewModels,
                cellFactory = FileListCellFactory(),
                fileDialogModel = null,
                onFileDialogDismissed = {},
                onOpenFileClicked = {},
                onOpenFileAsTextClicked = {},
            )
        }
    }
}

@ExperimentalFoundationApi
@Preview(name = "Data")
@Composable
fun FileListScreen_Data() {
    Preview(
        state = ScreenState.data(),
        cellViewModels = CELLS
    )
}

@ExperimentalFoundationApi
@Preview(name = "Data with Error")
@Composable
fun FileListScreen_DataWithError() {
    Preview(
        state = ScreenState.dataWithError("Error text"),
        cellViewModels = CELLS
    )
}

@ExperimentalFoundationApi
@Preview(name = "Empty")
@Composable
fun FileListScreen_Empty() {
    Preview(
        state = ScreenState.empty("No items"),
        cellViewModels = emptyList()
    )
}

@ExperimentalFoundationApi
@Preview(name = "Loading")
@Composable
fun FileListScreen_Loading() {
    Preview(
        state = ScreenState.loading(),
        cellViewModels = emptyList()
    )
}

@ExperimentalFoundationApi
@Preview(name = "Error")
@Composable
fun FileListScreen_Error() {
    Preview(
        state = ScreenState.error("Error text with detailed long message on the screen"),
        cellViewModels = emptyList()
    )
}
