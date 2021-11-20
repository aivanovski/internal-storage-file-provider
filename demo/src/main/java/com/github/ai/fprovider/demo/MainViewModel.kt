package com.github.ai.fprovider.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.ai.fprovider.InternalStorageTokenManager
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(
    private val interactor: FileSystemInteractor,
    private val tokenManager: InternalStorageTokenManager
) : ViewModel() {

    private val _isProgressVisible = MutableLiveData(false)
    val isProgressVisible: LiveData<Boolean> = _isProgressVisible

    private val _accessToken = MutableLiveData("")
    val accessToken: LiveData<String> = _accessToken

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

            _accessToken.value = newToken
            _isProgressVisible.value = false
        }
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
        private const val DEFAULT_PATH_TO_FILES = "/file/home"
    }
}

class MainViewModelFactory(
    private val interactor: FileSystemInteractor,
    private val tokenManager: InternalStorageTokenManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(interactor, tokenManager) as T
    }
}