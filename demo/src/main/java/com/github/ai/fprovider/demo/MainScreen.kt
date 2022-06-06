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
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val isProgressVisible: Boolean by viewModel.isProgressVisible.observeAsState(false)
    val isOpenButtonEnabled by viewModel.isOpenButtonEnabled.observeAsState(false)
    val message: String by viewModel.currentTokensText.observeAsState("")
    val generatedToken: String by viewModel.generatedTokenText.observeAsState("")

    MainScreenLayout(
        isProgressVisible = isProgressVisible,
        isOpenButtonEnabled = isOpenButtonEnabled,
        generatedTokenText = generatedToken,
        currentTokensText = message,
        onGenerateTokenClicked = { viewModel.onGenerateTokenClicked() },
        onLaunchViewerActivityClicked = { viewModel.onLaunchViewerActivityClicked() },
        onSetTokenViaManagerClicked = { viewModel.onSetTokenViaTokenManagerClicked() },
        onClearTokensViaManagerClicked = { viewModel.onRemoveAllTokensViaTokenManagerClicked() },
        onSetTokenViaBroadcastClicked = { viewModel.onSetTokenViaBroadcastClicked() },
        onClearTokensViaBroadcastClicked = { viewModel.onRemoveAllTokenViaBroadcastClicked() }
    )
}

@Composable
private fun MainScreenLayout(
    isProgressVisible: Boolean,
    isOpenButtonEnabled: Boolean,
    generatedTokenText: String,
    currentTokensText: String,
    onGenerateTokenClicked: (() -> Unit)? = null,
    onLaunchViewerActivityClicked: (() -> Unit)? = null,
    onSetTokenViaManagerClicked: (() -> Unit)? = null,
    onClearTokensViaManagerClicked: (() -> Unit)? = null,
    onSetTokenViaBroadcastClicked: (() -> Unit)? = null,
    onClearTokensViaBroadcastClicked: (() -> Unit)? = null,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (generateButton,
            generateText,
            setTokenViaManagerButton,
            setTokenViaBroadcastButton,
            clearTokensViaManagerButton,
            clearTokensViaBroadcastButton,
            viewerButton,
            tokenText,
            progress) = createRefs()

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
                onClick = { onGenerateTokenClicked?.invoke() },
                modifier = Modifier
                    .constrainAs(generateButton) {
                        top.linkTo(parent.top, margin = 24.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            ) {
                Text(text = "Generate token")
            }

            Text(
                text = generatedTokenText,
                fontSize = 20.sp,
                modifier = Modifier
                    .constrainAs(generateText) {
                        top.linkTo(generateButton.top)
                        start.linkTo(generateButton.end, margin = 16.dp)
                        bottom.linkTo(generateButton.bottom)
                    }
            )

            Button(
                onClick = { onSetTokenViaManagerClicked?.invoke() },
                modifier = Modifier
                    .constrainAs(setTokenViaManagerButton) {
                        top.linkTo(generateButton.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            ) {
                Text(text = "Set token via TokenManager")
            }

            Button(
                onClick = { onClearTokensViaManagerClicked?.invoke() },
                modifier = Modifier
                    .constrainAs(clearTokensViaManagerButton) {
                        top.linkTo(setTokenViaManagerButton.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            ) {
                Text(text = "Remove all tokens via TokenManager")
            }

            Button(
                onClick = { onSetTokenViaBroadcastClicked?.invoke() },
                modifier = Modifier
                    .constrainAs(setTokenViaBroadcastButton) {
                        top.linkTo(clearTokensViaManagerButton.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            ) {
                Text(text = "Set token via Broadcast")
            }

            Button(
                onClick = { onClearTokensViaBroadcastClicked?.invoke() },
                modifier = Modifier
                    .constrainAs(clearTokensViaBroadcastButton) {
                        top.linkTo(setTokenViaBroadcastButton.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            ) {
                Text(text = "Remove all tokens via Broadcast")
            }

            Text(
                text = currentTokensText,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .constrainAs(tokenText) {
                        top.linkTo(clearTokensViaBroadcastButton.bottom)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            )

            Button(
                onClick = { onLaunchViewerActivityClicked?.invoke() },
                enabled = isOpenButtonEnabled,
                modifier = Modifier
                    .constrainAs(viewerButton) {
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            ) {
                Text(text = "Launch viewer Activity")
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
            generatedTokenText = "Generated: 1111",
            currentTokensText = "Current tokens:\n1111\n2222"
        )
    }
}