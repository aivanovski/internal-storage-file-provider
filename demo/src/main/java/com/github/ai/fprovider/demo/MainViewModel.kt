package com.github.ai.fprovider.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val interactor: FileSystemInteractor) : ViewModel() {

    private val _isProgressVisible = MutableLiveData(false)
    val isProgressVisible: LiveData<Boolean> = _isProgressVisible

    fun createFileStructure() {
        _isProgressVisible.value = true

        viewModelScope.launch {
            interactor.createFiles()
            _isProgressVisible.value = false
        }
    }
}

class MainViewModelFactory(
    private val interactor: FileSystemInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(interactor) as T
    }
}