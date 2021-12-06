package com.github.ai.fprovider.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import com.github.ai.isfprovider.InternalStorageTokenManager

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels(
        factoryProducer = {
            MainViewModelFactory(
                FileSystemInteractor(applicationContext),
                InternalStorageTokenManager.from(applicationContext)
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ActivityScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ActivityScreen(viewModel: MainViewModel) {
    val state: Boolean by viewModel.isProgressVisible.observeAsState(false)
    val accessToken: String by viewModel.accessToken.observeAsState("")
    ScreenLayout(
        isProgressVisible = state,
        accessToken = accessToken,
        onGenerateStructureClicked = { viewModel.createFileStructure() },
        onGenerateTokenClicked = { viewModel.generateAccessToken() }
    )
}

@Composable
fun ScreenLayout(
    isProgressVisible: Boolean,
    accessToken: String,
    onGenerateStructureClicked: (() -> Unit)? = null,
    onGenerateTokenClicked: (() -> Unit)? = null
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (fileStructureButton, tokenButton, tokenText, progress) = createRefs()

        createVerticalChain(
            fileStructureButton,
            tokenButton,
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
                    bottom.linkTo(tokenButton.top)
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
                        bottom.linkTo(tokenText.top)
                    }
            ) {
                Text(text = "Set new token")
            }

            if (accessToken.isNotEmpty()) {
                Text(
                    text = "New access token is set: $accessToken",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .constrainAs(tokenText) {
                            top.linkTo(tokenButton.bottom)
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
        ScreenLayout(
            isProgressVisible = false,
            accessToken = "abc-123"
        )
    }
}
