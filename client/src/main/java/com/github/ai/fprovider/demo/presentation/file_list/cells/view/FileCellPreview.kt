package com.github.ai.fprovider.demo.presentation.file_list.cells.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.ai.fprovider.demo.R
import com.github.ai.fprovider.demo.presentation.file_list.cells.model.FileCellModel
import com.github.ai.fprovider.demo.presentation.file_list.cells.viewmodel.FileCellViewModel
import com.github.ai.fprovider.demo.presentation.theme.AppTheme
import com.github.ai.fprovider.demo.presentation.theme.backgroundColor

val FILE_MODEL = FileCellModel(
    id = "1",
    name = "image.jpg",
    description = "12 kB",
    iconResId = R.drawable.ic_file_white_24dp
)

val DIRECTORY_MODEL = FileCellModel(
    id = "2",
    name = "Downloads/",
    description = "folder",
    iconResId = R.drawable.ic_folder_white_24dp
)

@Preview
@Composable
fun FileCellPreviewWithFile() {
    Preview(model = FILE_MODEL)
}

@Preview
@Composable
fun FileCellPreviewWithDir() {
    Preview(model = DIRECTORY_MODEL)
}

@Composable
private fun Preview(model: FileCellModel) {
    AppTheme {
        Box(modifier = Modifier.background(backgroundColor)) {
            FileCell(FileCellViewModel(model))
        }
    }
}
