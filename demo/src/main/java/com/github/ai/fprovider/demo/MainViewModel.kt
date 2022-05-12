package com.github.ai.fprovider.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.ai.fprovider.demo.utils.Event
import com.github.ai.fprovider.demo.utils.ResourceProvider
import com.github.ai.isfprovider.InternalStorageTokenManager
import kotlinx.coroutines.launch
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

    private val _accessTokenMessage = MutableLiveData("")
    val accessTokenMessage: LiveData<String> = _accessTokenMessage

    private val _showViewerEvent = MutableLiveData<Event<AccessData>>()
    val showViewerEvent: LiveData<Event<AccessData>> = _showViewerEvent

    private var token: String? = null

    fun loadCurrentToken() {
        viewModelScope.launch {
            val currentToken = tokenManager.getAllTokens().firstOrNull()
            token = currentToken
            _isOpenButtonEnabled.value = (token != null)

            if (currentToken != null) {
                _accessTokenMessage.value = "Current token is: $currentToken"
            } else {
                _accessTokenMessage.value = "Access token is not set"
            }
        }
    }

    fun createFileStructure() {
        _isProgressVisible.value = true

        viewModelScope.launch {
            interactor.createFilesInsideInternalStorage()
            _isProgressVisible.value = false
        }
    }

    fun generateAccessToken() {
        _isProgressVisible.value = true

        viewModelScope.launch {
            tokenManager.removeAllTokens()

            val newToken = newToken()
            tokenManager.addToken(newToken, DEFAULT_PATH_TO_FILES)

            token = newToken
            _isOpenButtonEnabled.value = (token != null)
            _accessTokenMessage.value = "Generated new token: $newToken"
            _isProgressVisible.value = false
        }
    }

    fun showViewer() {
        val token = token ?: return

        _showViewerEvent.value = Event(
            AccessData(
                contentProviderAuthority = resourceProvider.getString(R.string.content_provider_authority),
                path = "$DEFAULT_PATH_TO_FILES/*",
                token = token
            )
        )
    }

    private fun newToken(): String {
        val value = Random(System.currentTimeMillis()).nextInt(10)

        val token = StringBuilder()
        for (idx in 0 until 4) {
            token.append(value)
        }

        return token.toString()
    }

    data class AccessData(
        val contentProviderAuthority: String,
        val path: String,
        val token: String
    )

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