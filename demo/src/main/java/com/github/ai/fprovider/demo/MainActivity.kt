package com.github.ai.fprovider.demo

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import com.github.ai.fprovider.demo.utils.EventObserver
import com.github.ai.fprovider.demo.utils.ResourceProvider
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver
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
    private val receiver = InternalStorageBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MainScreen(viewModel = viewModel)
            }
        }

        subscribeToEvents()
        viewModel.start()

        registerReceiver(
            receiver,
            IntentFilter(getString(R.string.broadcast_receiver_action))
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCurrentToken()
    }

    private fun subscribeToEvents() {
        viewModel.sendBroadcastIntentEvent.observe(this, EventObserver {
            sendBroadcast(it)
        })
    }
}
