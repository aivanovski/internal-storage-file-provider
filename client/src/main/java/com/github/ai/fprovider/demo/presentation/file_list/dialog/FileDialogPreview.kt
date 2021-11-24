package com.github.ai.fprovider.demo.presentation.file_list.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.ai.fprovider.demo.data.entity.FileEntity
import com.github.ai.fprovider.demo.presentation.theme.AppTheme
import com.github.ai.fprovider.demo.presentation.theme.backgroundColor

private val IMAGE_FILE = FileEntity(
    path = "path",
    name = "image.jpeg",
    size = 128,
    mimeType = "image/jpeg",
    isDirectory = false
)

@Composable
@Preview
fun FileDialogContentPreview() {
    Preview(
        IMAGE_FILE
    )
}

@Composable
private fun Preview(
    file: FileEntity
) {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            FileDialogContent(
                file = file
            )
        }
    }
}