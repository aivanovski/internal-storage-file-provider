package com.github.ai.fprovider.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels(
        factoryProducer = {
            MainViewModelFactory(
                FileSystemInteractor(applicationContext)
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
    ScreenLayout(
        isProgressVisible = state,
        onButtonClicked = { viewModel.createFileStructure() }
    )
}

@Composable
fun ScreenLayout(
    isProgressVisible: Boolean,
    onButtonClicked: (() -> Unit)? = null
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (button, progress) = createRefs()

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
                onClick = { onButtonClicked?.invoke() },
                modifier = Modifier.constrainAs(button) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            ) {
                Text(text = "Create file structure")
            }
        }
    }
}

@Preview
@Composable
fun LayoutPreview() {
    MaterialTheme {
        ScreenLayout(isProgressVisible = false)
    }
}
