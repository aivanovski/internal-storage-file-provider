package com.github.ai.fprovider.demo.presentation.file_list.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.ai.fprovider.demo.R
import com.github.ai.fprovider.demo.data.entity.FileEntity
import com.github.ai.fprovider.demo.extension.toFilePath
import com.github.ai.fprovider.demo.extension.toUri
import com.github.ai.fprovider.demo.utils.StringUtils
import com.github.ai.fprovider.demo.utils.StringUtils.EMPTY

@Composable
fun FileDialog(
    file: FileEntity,
    onDismissed: () -> Unit,
    onOpenClicked: (file: FileEntity) -> Unit,
    onOpenAsTextClicked: (file: FileEntity) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismissed.invoke()
        },
        title = {
            Text(
                text = stringResource(R.string.properties),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            FileDialogContent(file)
        },
        buttons = {
            if (!file.isDirectory) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        )
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onDismissed.invoke()
                            onOpenClicked.invoke(file)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.open)
                        )
                    }
                    Button(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        onClick = {
                            onDismissed.invoke()
                            onOpenAsTextClicked.invoke(file)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.open_as_text)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun FileDialogContent(file: FileEntity) {
    Column {
        TextRow(
            title = stringResource(R.string.str_with_dots, stringResource(R.string.name)),
            text = file.name
        )
        TextRow(
            title = stringResource(R.string.str_with_dots, stringResource(R.string.path)),
            text = file.toFilePath(accessToken = EMPTY).path
        )
        TextRow(
            title = stringResource(R.string.str_with_dots, stringResource(R.string.uri)),
            text = file.toFilePath(accessToken = "****").toUri().toString()
        )
        if (!file.isDirectory) {
            TextRow(
                title = stringResource(R.string.str_with_dots, stringResource(R.string.size)),
                text = StringUtils.formatFileSize(file.size ?: 0)
            )
        }
        TextRow(
            title = stringResource(R.string.str_with_dots, stringResource(R.string.mime_type)),
            text = file.mimeType ?: EMPTY
        )
    }
}

@Composable
private fun TextRow(title: String, text: String) {
    Row {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(fraction = 0.3f)
        )
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(fraction = 1f)
        )
    }
}