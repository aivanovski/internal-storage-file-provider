package com.github.ai.fprovider.client.presentation.file_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.github.ai.fprovider.client.data.entity.FileEntity
import com.github.ai.fprovider.client.presentation.core.cells.BaseCellViewModel
import com.github.ai.fprovider.client.presentation.core.model.ScreenState
import com.github.ai.fprovider.client.presentation.core.model.ScreenStateType.DATA
import com.github.ai.fprovider.client.presentation.core.model.ScreenStateType.DATA_WITH_ERROR
import com.github.ai.fprovider.client.presentation.core.model.ScreenStateType.EMPTY
import com.github.ai.fprovider.client.presentation.core.model.ScreenStateType.ERROR
import com.github.ai.fprovider.client.presentation.core.model.ScreenStateType.LOADING
import com.github.ai.fprovider.client.presentation.file_list.cells.FileListCellFactory
import com.github.ai.fprovider.client.presentation.file_list.dialog.FileDialog
import com.github.ai.fprovider.client.presentation.file_list.model.FileDialogModel
import com.github.ai.fprovider.client.presentation.theme.dividerColor
import com.github.ai.fprovider.client.utils.StringUtils

@ExperimentalFoundationApi
@Composable
fun FileListScreen(viewModel: FileListViewModel) {
    val state by viewModel.screenState.observeAsState(ScreenState.loading())
    val cellViewModels by viewModel.cellViewModels.observeAsState(emptyList())
    val fileDialogModel by viewModel.showFileDialogEvent.observeAsState()
    val cellFactory = FileListCellFactory()

    FileListLayout(
        state = state,
        cellViewModels = cellViewModels,
        cellFactory = cellFactory,
        fileDialogModel = fileDialogModel,
        onFileDialogDismissed = {
            viewModel.onFileDialogDismissed()
        },
        onOpenFileClicked = {
            viewModel.onOpenFileClicked(it)
        },
        onOpenFileAsTextClicked = {
            viewModel.onOpenFileAsTextClicked(it)
        }
    )
}

@ExperimentalFoundationApi
@Composable
fun FileListLayout(
    state: ScreenState,
    cellViewModels: List<BaseCellViewModel>,
    cellFactory: FileListCellFactory,
    fileDialogModel: FileDialogModel?,
    onFileDialogDismissed: () -> Unit,
    onOpenFileClicked: (file: FileEntity) -> Unit,
    onOpenFileAsTextClicked: (file: FileEntity) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (list, progress, emptyText, errorText) = createRefs()

        when (state.type) {
            DATA, DATA_WITH_ERROR -> {
                Column(
                    modifier = Modifier
                        .constrainAs(list) {
                            linkTo(
                                start = parent.start,
                                top = parent.top,
                                end = parent.end,
                                bottom = parent.bottom
                            )
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                ) {
                    if (state.type == DATA_WITH_ERROR) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray)
                                .defaultMinSize(minHeight = 96.dp)
                        ) {
                            Text(
                                text = state.errorText ?: StringUtils.EMPTY,
                                fontSize = 24.sp,
                                color = Color(0xFF_C0_00_20),
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
                            )
                        }
                    }
                    LazyColumn {
                        items(cellViewModels) { viewModel ->
                            cellFactory.createCell(viewModel)
                            Divider(color = dividerColor)
                        }
                    }
                }
            }
            LOADING -> {
                CircularProgressIndicator(
                    modifier = Modifier.constrainAs(progress) {
                        linkTo(
                            start = parent.start,
                            top = parent.top,
                            end = parent.end,
                            bottom = parent.bottom
                        )
                    }
                )
            }
            EMPTY -> {
                Text(
                    text = state.emptyText ?: StringUtils.EMPTY,
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.constrainAs(emptyText) {
                        linkTo(
                            start = parent.start,
                            top = parent.top,
                            end = parent.end,
                            bottom = parent.bottom
                        )
                    }
                )
            }
            ERROR -> {
                Text(
                    text = state.errorText ?: StringUtils.EMPTY,
                    fontSize = 24.sp,
                    color = Color(0xFF_C0_00_20),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .constrainAs(errorText) {
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                )
            }
        }
    }

    fileDialogModel?.let {
        FileDialog(
            file = it.file,
            onDismissed = onFileDialogDismissed,
            onOpenClicked = onOpenFileClicked,
            onOpenAsTextClicked = onOpenFileAsTextClicked
        )
    }
}