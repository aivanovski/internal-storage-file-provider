package com.github.ai.fprovider.client.presentation.file_list.cells.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.ai.fprovider.client.R
import com.github.ai.fprovider.client.presentation.file_list.cells.model.QuestionCellModel
import com.github.ai.fprovider.client.presentation.file_list.cells.viewmodel.QuestionCellViewModel
import com.github.ai.fprovider.client.presentation.theme.AppTheme
import com.github.ai.fprovider.client.presentation.theme.backgroundColor
import com.github.ai.fprovider.client.presentation.theme.primaryTextColor

@Composable
fun QuestionCell(viewModel: QuestionCellViewModel) {
    Column(
        modifier = Modifier
    ) {
        Text(
            text = viewModel.model.message,
            color = primaryTextColor,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
        )

        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp, end = 16.dp)
        ) {
            Row {
                Button(
                    onClick = { viewModel.model.onNegativeClicked.invoke() }
                ) {
                    Text(text = viewModel.model.negativeText)
                }

                Button(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    onClick = { viewModel.model.onPositiveClicked.invoke() }
                ) {
                    Text(text = viewModel.model.positiveText)
                }
            }
        }

    }
}

@Composable
@Preview
fun Preview() {
    val model = QuestionCellModel(
        message = stringResource(R.string.save_data_question),
        positiveText = stringResource(R.string.yes),
        negativeText = stringResource(R.string.no),
        onNegativeClicked = {},
        onPositiveClicked = {}
    )

    AppTheme {
        Box(modifier = Modifier.background(backgroundColor)) {
            QuestionCell(QuestionCellViewModel(model))
        }
    }
}