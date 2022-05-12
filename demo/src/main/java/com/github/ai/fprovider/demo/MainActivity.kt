package com.github.ai.fprovider.demo

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import com.github.ai.fprovider.demo.MainViewModel.AccessData
import com.github.ai.fprovider.demo.utils.EventObserver
import com.github.ai.fprovider.demo.utils.ResourceProvider
import com.github.ai.isfprovider.InternalStorageFileProvider
import com.github.ai.isfprovider.InternalStorageFileProvider.Companion.VIEWER_EXTRA_AUTHORITY
import com.github.ai.isfprovider.InternalStorageFileProvider.Companion.VIEWER_EXTRA_ROOT_PATH
import com.github.ai.isfprovider.InternalStorageFileProvider.Companion.VIEWER_EXTRA_ACCESS_TOKEN
import com.github.ai.isfprovider.InternalStorageTokenManager

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels(
        factoryProducer = {
            MainViewModelFactory(
                FileSystemInteractor(applicationContext),
                InternalStorageTokenManager.from(applicationContext),
                ResourceProvider(applicationContext)
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MainScreen(viewModel = viewModel)
            }
        }

        subscribeToEvents()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCurrentToken()
    }

    private fun subscribeToEvents() {
        viewModel.showViewerEvent.observe(this, EventObserver {
            showViewerActivity(it)
        })
    }

    private fun showViewerActivity(data: AccessData) {
        val intent = Intent()
            .apply {
                action = InternalStorageFileProvider.LAUNCH_VIEWER_INTENT_ACTION
                putExtra(VIEWER_EXTRA_AUTHORITY, data.contentProviderAuthority)
                putExtra(VIEWER_EXTRA_ROOT_PATH, data.path)
                putExtra(VIEWER_EXTRA_ACCESS_TOKEN, data.token)
            }

        startActivity(Intent.createChooser(intent, "Select app to open"))
    }
}
