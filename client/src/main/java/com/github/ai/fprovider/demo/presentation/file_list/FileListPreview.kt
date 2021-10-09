package com.github.ai.fprovider.demo.presentation.file_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.ai.fprovider.demo.presentation.core.model.ScreenState
import com.github.ai.fprovider.demo.presentation.file_list.cells.DIRECTORY_MODEL
import com.github.ai.fprovider.demo.presentation.file_list.cells.FILE_MODEL
import com.github.ai.fprovider.demo.presentation.file_list.cells.FileCellViewModel
import com.github.ai.fprovider.demo.presentation.theme.AppTheme
import com.github.ai.fprovider.demo.presentation.theme.backgroundColor

private val CELLS = listOf(
    FileCellViewModel(DIRECTORY_MODEL),
    FileCellViewModel(FILE_MODEL)
)

@Composable
private fun Preview(
    state: ScreenState,
    cells: List<FileCellViewModel>
) {
    AppTheme {
        Box(modifier = Modifier.background(backgroundColor)) {
            FileListLayout(state, cells)
        }
    }
}

@Preview(name = "Data")
@Composable
fun FileListScreen_Data() {
    Preview(
        state = ScreenState.data(),
        cells = CELLS
    )
}

@Preview(name = "Data with Error")
@Composable
fun FileListScreen_DataWithError() {
    Preview(
        state = ScreenState.dataWithError("Error text"),
        cells = CELLS
    )
}

@Preview(name = "Empty")
@Composable
fun FileListScreen_Empty() {
    Preview(
        state = ScreenState.empty("No items"),
        cells = emptyList()
    )
}

@Preview(name = "Loading")
@Composable
fun FileListScreen_Loading() {
    Preview(
        state = ScreenState.loading(),
        cells = emptyList()
    )
}

@Preview(name = "Error")
@Composable
fun FileListScreen_Error() {
    Preview(
        state = ScreenState.error("Error text"),
        cells = emptyList()
    )
}
