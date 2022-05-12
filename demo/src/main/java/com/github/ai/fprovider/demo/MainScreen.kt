package com.github.ai.fprovider.demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val isProgressVisible: Boolean by viewModel.isProgressVisible.observeAsState(false)
    val isOpenButtonEnabled by viewModel.isOpenButtonEnabled.observeAsState(false)
    val message: String by viewModel.accessTokenMessage.observeAsState("")

    MainScreenLayout(
        isProgressVisible = isProgressVisible,
        isOpenButtonEnabled = isOpenButtonEnabled,
        message = message,
        onGenerateStructureClicked = { viewModel.createFileStructure() },
        onGenerateTokenClicked = { viewModel.generateAccessToken() },
        onOpenWithViewerClicked =  { viewModel.showViewer() }
    )
}

@Composable
private fun MainScreenLayout(
    isProgressVisible: Boolean,
    isOpenButtonEnabled: Boolean,
    message: String,
    onGenerateStructureClicked: (() -> Unit)? = null,
    onGenerateTokenClicked: (() -> Unit)? = null,
    onOpenWithViewerClicked: (() -> Unit)? = null
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (fileStructureButton, tokenButton, viewerButton, tokenText, progress) = createRefs()

        createVerticalChain(
            fileStructureButton,
            tokenButton,
            viewerButton,
            tokenText,
            chainStyle = ChainStyle.Packed
        )

        if (isProgressVisible) {
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(progress) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )
        } else {
            Button(
                onClick = { onGenerateStructureClicked?.invoke() },
                modifier = Modifier.constrainAs(fileStructureButton) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                Text(text = "Create file structure")
            }

            Button(
                onClick = { onGenerateTokenClicked?.invoke() },
                modifier = Modifier
                    .padding(top = 12.dp)
                    .constrainAs(tokenButton) {
                        top.linkTo(fileStructureButton.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(text = "Generate new token")
            }

            Button(
                onClick = { onOpenWithViewerClicked?.invoke() },
                enabled = isOpenButtonEnabled,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .constrainAs(viewerButton) {
                        top.linkTo(tokenButton.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(text = "Open files with viewer")
            }

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .constrainAs(tokenText) {
                            top.linkTo(viewerButton.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun LayoutPreview() {
    MaterialTheme {
        MainScreenLayout(
            isProgressVisible = false,
            isOpenButtonEnabled = true,
            message = "Message"
        )
    }
}