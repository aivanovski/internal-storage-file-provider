package com.github.ai.fprovider.demo

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.ai.fprovider.demo.utils.Event
import com.github.ai.fprovider.demo.utils.ResourceProvider
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.COMMAND_ADD_TOKEN
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.COMMAND_LAUNCH_VIEWER
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.COMMAND_REMOVE_ALL_TOKENS
import com.github.ai.isfprovider.InternalStorageTokenManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random

class MainViewModel(
    private val interactor: FileSystemInteractor,
    private val tokenManager: InternalStorageTokenManager,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _isProgressVisible = MutableLiveData(false)
    val isProgressVisible: LiveData<Boolean> = _isProgressVisible

    private val _isOpenButtonEnabled = MutableLiveData(false)
    val isOpenButtonEnabled: LiveData<Boolean> = _isOpenButtonEnabled

    private val _currentTokensText = MutableLiveData("")
    val currentTokensText: LiveData<String> = _currentTokensText

    private val _generatedTokenText = MutableLiveData("")
    val generatedTokenText: LiveData<String> = _generatedTokenText

    private val _sendBroadcastIntentEvent = MutableLiveData<Event<Intent>>()
    val sendBroadcastIntentEvent: LiveData<Event<Intent>> = _sendBroadcastIntentEvent

    private var token: String? = null
    private var generatedToken: String? = null
    private val shouldCheckToken = AtomicBoolean(true)
    private var periodicJob: Job? = null

    fun start() {
        _isProgressVisible.value = true

        viewModelScope.launch {
            if (!interactor.isFilesCreated()) {
                interactor.createFilesInsideInternalStorage()
            }
            _isProgressVisible.value = false

            loadCurrentToken()
        }

        shouldCheckToken.set(true)
        periodicJob = viewModelScope.launch {
            while (shouldCheckToken.get()) {
                delay(1000L)
                loadCurrentToken()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        shouldCheckToken.set(false)
        periodicJob?.cancel()
        periodicJob = null
    }

    fun loadCurrentToken() {
        viewModelScope.launch {
            val currentToken = tokenManager.getAllTokens().firstOrNull()
            token = currentToken
            _isOpenButtonEnabled.value = (token != null)

            if (currentToken != null) {
                _currentTokensText.value = "Current token is: $currentToken"
            } else {
                _currentTokensText.value = "Access token is not set"
            }
        }
    }

    fun onGenerateTokenClicked() {
        val newToken = newToken().apply {
            generatedToken = this
        }

        _generatedTokenText.value = "Generated: $newToken"
    }

    fun onSetTokenViaTokenManagerClicked() {
        val token = generatedToken ?: return

        tokenManager.removeAllTokens()
        tokenManager.addToken(token, DEFAULT_PATH_TO_FILES)

        loadCurrentToken()
    }

    fun onRemoveAllTokensViaTokenManagerClicked() {
        tokenManager.removeAllTokens()
        loadCurrentToken()
    }

    fun onSetTokenViaBroadcastClicked() {
        val token = generatedToken ?: return
        val path = "$DEFAULT_PATH_TO_FILES/*"

        val intent = Intent()
            .apply {
                action = resourceProvider.getString(R.string.broadcast_receiver_action)
                putExtra(InternalStorageBroadcastReceiver.EXTRA_COMMAND, COMMAND_ADD_TOKEN)
                putExtra(InternalStorageBroadcastReceiver.EXTRA_AUTH_TOKEN, token)
                putExtra(InternalStorageBroadcastReceiver.EXTRA_PATH, path)
            }

        _sendBroadcastIntentEvent.value = Event(intent)
    }

    fun onRemoveAllTokenViaBroadcastClicked() {
        val intent = Intent()
            .apply {
                action = resourceProvider.getString(R.string.broadcast_receiver_action)
                putExtra(InternalStorageBroadcastReceiver.EXTRA_COMMAND, COMMAND_REMOVE_ALL_TOKENS)
            }

        _sendBroadcastIntentEvent.value = Event(intent)
    }

    fun onLaunchViewerActivityClicked() {
        val token = token ?: return

        val authority = resourceProvider.getString(R.string.content_provider_authority)
        val path = "$DEFAULT_PATH_TO_FILES/*"

        val intent = Intent()
            .apply {
                action = resourceProvider.getString(R.string.broadcast_receiver_action)
                putExtra(InternalStorageBroadcastReceiver.EXTRA_COMMAND, COMMAND_LAUNCH_VIEWER)
                putExtra(InternalStorageBroadcastReceiver.EXTRA_AUTHORITY, authority)
                putExtra(InternalStorageBroadcastReceiver.EXTRA_PATH, path)
                putExtra(InternalStorageBroadcastReceiver.EXTRA_AUTH_TOKEN, token)
            }

        _sendBroadcastIntentEvent.value = Event(intent)
    }

    private fun newToken(): String {
        val value = Random(System.currentTimeMillis()).nextInt(10)

        val token = StringBuilder()
        for (idx in 0 until 4) {
            token.append(value)
        }

        return token.toString()
    }

    companion object {
        private const val DEFAULT_PATH_TO_FILES = "/files/home"
    }
}

class MainViewModelFactory(
    private val interactor: FileSystemInteractor,
    private val tokenManager: InternalStorageTokenManager,
    private val resourceProvider: ResourceProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(interactor, tokenManager, resourceProvider) as T
    }
}