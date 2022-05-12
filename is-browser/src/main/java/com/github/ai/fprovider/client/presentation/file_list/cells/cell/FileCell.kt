package com.github.ai.fprovider.client.presentation.file_list.cells.cell

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.github.ai.fprovider.client.R
import com.github.ai.fprovider.client.presentation.file_list.cells.model.FileCellModel
import com.github.ai.fprovider.client.presentation.file_list.cells.viewmodel.FileCellViewModel
import com.github.ai.fprovider.client.presentation.theme.AppTheme
import com.github.ai.fprovider.client.presentation.theme.backgroundColor

@ExperimentalFoundationApi
@Composable
fun FileCell(viewModel: FileCellViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = {
                    viewModel.model.onClick?.invoke(
                        viewModel.model.id
                    )
                },
                onLongClick = {
                    viewModel.model.onLongClick?.invoke(
                        viewModel.model.id
                    )
                }
            )
    ) {
        val (icon, iconBackground, preview, title, description) = createRefs()

        createVerticalChain(title, description, chainStyle = ChainStyle.Packed)

        val iconBarrier = createEndBarrier(icon, preview)

        if (viewModel.model.imageUri == null) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color.LightGray)
                    .constrainAs(iconBackground) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            )

            Image(
                painter = painterResource(id = viewModel.model.iconResId),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(iconBackground.top)
                        bottom.linkTo(iconBackground.bottom)
                        start.linkTo(iconBackground.start)
                        end.linkTo(iconBackground.end)
                    }
            )
        } else {
            Image(
                painter = rememberImagePainter(
                    data = viewModel.model.imageUri,
                    builder = {
                        transformations(CircleCropTransformation())
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .constrainAs(preview) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            )
        }

        Text(
            text = viewModel.model.name,
            fontSize = 18.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    bottom.linkTo(description.top)
                    start.linkTo(iconBarrier, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                }
        )

        Text(
            text = viewModel.model.description,
            fontSize = 14.sp,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(description) {
                    top.linkTo(title.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(title.start)
                    end.linkTo(title.end)
                    width = Dimension.fillToConstraints
                }
        )
    }
}

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

@ExperimentalFoundationApi
@Preview
@Composable
fun FileCellPreviewWithFile() {
    Preview(model = FILE_MODEL)
}

@ExperimentalFoundationApi
@Preview
@Composable
fun FileCellPreviewWithDir() {
    Preview(model = DIRECTORY_MODEL)
}

@ExperimentalFoundationApi
@Composable
private fun Preview(model: FileCellModel) {
    AppTheme {
        Box(modifier = Modifier.background(backgroundColor)) {
            FileCell(FileCellViewModel(model))
        }
    }
}