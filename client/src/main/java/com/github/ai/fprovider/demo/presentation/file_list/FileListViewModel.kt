package com.github.ai.fprovider.demo.presentation.file_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ai.fprovider.demo.R
import com.github.ai.fprovider.demo.data.entity.FileEntity
import com.github.ai.fprovider.demo.data.entity.FilePath
import com.github.ai.fprovider.demo.domain.ErrorInteractor
import com.github.ai.fprovider.demo.domain.OnSettingsChangeListener
import com.github.ai.fprovider.demo.domain.ResourceProvider
import com.github.ai.fprovider.demo.domain.Settings
import com.github.ai.fprovider.demo.domain.file_list.FileListInteractor
import com.github.ai.fprovider.demo.extension.toFilePath
import com.github.ai.fprovider.demo.extension.toPath
import com.github.ai.fprovider.demo.extension.toUri
import com.github.ai.fprovider.demo.presentation.Screens.SettingsScreen
import com.github.ai.fprovider.demo.presentation.core.model.ScreenState
import com.github.ai.fprovider.demo.presentation.core.model.ScreenStateType.DATA
import com.github.ai.fprovider.demo.presentation.core.model.ScreenStateType.DATA_WITH_ERROR
import com.github.ai.fprovider.demo.presentation.file_list.cells.FileListCellFactory
import com.github.ai.fprovider.demo.presentation.file_list.cells.viewmodel.FileCellViewModel
import com.github.ai.fprovider.demo.presentation.file_list.model.FileDialogModel
import com.github.ai.fprovider.demo.presentation.file_list.model.OpenFileModel
import com.github.ai.fprovider.demo.utils.Event
import com.github.ai.fprovider.demo.utils.StringUtils.EMPTY
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch

class FileListViewModel(
    private val interactor: FileListInteractor,
    private val cellFactory: FileListCellFactory,
    private val errorInteractor: ErrorInteractor,
    private val resourceProvider: ResourceProvider,
    private val router: Router,
    private val settings: Settings
) : ViewModel() {

    val screenState = MutableLiveData(ScreenState.loading())
    val cellViewModels = MutableLiveData<List<FileCellViewModel>>()
    val actionBarTitle = MutableLiveData(resourceProvider.getString(R.string.app_name))
    val isActionBarBackButtonVisible = MutableLiveData(false)
    val showToastMessageEvent = MutableLiveData<Event<String>>()
    val openFileEvent = MutableLiveData<Event<OpenFileModel>>()
    val showFileDialogEvent = MutableLiveData<FileDialogModel>()

    private var isExitOnBack = false
    private var parentDir: FileEntity? = null
    private var currentDir: FileEntity? = null
    private val parents = mutableListOf<FileEntity>()
    private val settingsListener = OnSettingsChangeListener { onSettingsChanged(it) }
    private var currentPath = FilePath(
        authority = readContentProviderAuthority(),
        path = readRootPath(),
        accessToken = readAccessToken()
    )

    init {
        settings.registerListener(settingsListener)
    }

    override fun onCleared() {
        settings.unregisterListener(settingsListener)
    }

    fun loadData() {
        screenState.value = ScreenState.loading()

        viewModelScope.launch {
            if (currentDir == null) {
                val getCurrentDir = interactor.getFile(
                    currentPath.toFilePath(
                        accessToken = readAccessToken()
                    )
                )
                if (getCurrentDir.isSuccess) {
                    currentDir = getCurrentDir.getOrThrow()
                } else {
                    showError(getCurrentDir.getExceptionOrThrow())
                }
            }

            if (currentDir == null) {
                return@launch
            }

            val getFilesResult = interactor.getFileList(currentPath)
            if (getFilesResult.isSuccess) {
                val files = getFilesResult.getOrThrow()

                val cellModels = cellFactory.createCellModels(
                    parent = parentDir,
                    files = files,
                    onFileClicked = { file -> onFileClicked(file) },
                    onFileLongClicked = { file -> onFileLongClicked(file) }
                )

                cellViewModels.value = cellModels.map { FileCellViewModel(it) }

                screenState.value = ScreenState.data()
            } else {
                showError(getFilesResult.getExceptionOrThrow())
            }
        }
    }

    fun onBackClicked() {
        val screenStateType = screenState.value?.type ?: return

        when (screenStateType) {
            DATA, DATA_WITH_ERROR -> {
                if (canGoBack()) {
                    goBack()
                } else {
                    if (isExitOnBack) {
                        router.exit()
                    } else {
                        isExitOnBack = true
                        showToastMessageEvent.value = Event(
                            resourceProvider.getString(R.string.press_again_to_exit)
                        )
                    }
                }
            }
            else -> {
                if (isExitOnBack) {
                    router.exit()
                } else {
                    isExitOnBack = true
                    showToastMessageEvent.value = Event(
                        resourceProvider.getString(R.string.press_again_to_exit)
                    )
                }
            }
        }
    }

    fun onSettingsButtonClicked() {
        router.navigateTo(SettingsScreen())
    }

    fun onFileDialogDismissed() {
        showFileDialogEvent.value = null
    }

    fun onOpenFileClicked(file: FileEntity) {
        val uri = file.toPath(readAccessToken()).toUri()
        val mimeType = getMimeTypeForFile(file)
        openFileEvent.value = Event(OpenFileModel(uri, mimeType))
    }

    fun onOpenFileAsTextClicked(file: FileEntity) {
        val uri = file.toPath(readAccessToken()).toUri()
        openFileEvent.value = Event(OpenFileModel(uri, MIME_TYPE_TEXT))
    }

    private fun showError(error: Exception) {
        val message = errorInteractor.getMessage(error)
        screenState.value = ScreenState.error(message)
    }

    private fun onFileClicked(file: FileEntity) {
        isExitOnBack = false

        when {
            file == parentDir -> onParentDirectoryClicked()
            file.isDirectory -> onDirectoryClicked(file)
            else -> {
                openFileEvent.value = Event(
                    OpenFileModel(
                        uri = file.toPath(accessToken = readAccessToken()).toUri(),
                        mimeType = getMimeTypeForFile(file)
                    )
                )
            }
        }
    }

    private fun onFileLongClicked(file: FileEntity) {
        showFileDialogEvent.value = FileDialogModel(file)
    }

    private fun getMimeTypeForFile(file: FileEntity): String {
        return if (file.mimeType.isNullOrBlank()) {
            MIME_TYPE_TEXT
        } else {
            file.mimeType
        }
    }

    private fun onParentDirectoryClicked() {
        if (parents.size >= 2) {
            val last = parents.last()
            val beforeLast = parents[parents.size - 2]

            onDirectoryChanged(last, beforeLast)
        } else if (parents.size > 0) {
            val last = parents.last()

            onDirectoryChanged(last, null)
        }

        if (parents.isNotEmpty()) {
            parents.removeAt(parents.size - 1)
        }

        loadData()
    }

    private fun onDirectoryClicked(dir: FileEntity) {
        val currentDir = this.currentDir ?: return

        parents.add(currentDir)
        onDirectoryChanged(dir, currentDir)

        loadData()
    }

    private fun onDirectoryChanged(currentDir: FileEntity, parentDir: FileEntity?) {
        this.currentDir = currentDir
        this.parentDir = parentDir

        currentPath = currentDir.toPath(accessToken = readAccessToken())
        isActionBarBackButtonVisible.value = (parentDir != null)
        actionBarTitle.value = if (parentDir != null) {
            currentDir.name
        } else {
            resourceProvider.getString(R.string.app_name)
        }
    }

    private fun onSettingsChanged(type: Settings.Type) {
        when (type) {
            Settings.Type.ACCESS_TOKEN -> {
                currentPath = currentPath.copy(
                    accessToken = readAccessToken()
                )
                loadData()
            }
            Settings.Type.ROOT_PATH -> {
                if (parents.isEmpty()) {
                    currentPath = currentPath.copy(
                        path = readRootPath()
                    )
                }
                loadData()
            }
            Settings.Type.CONTENT_PROVIDER_AUTHORITY -> {
                currentPath = currentPath.copy(
                    authority = readContentProviderAuthority()
                )
                loadData()
            }
        }
    }

    private fun canGoBack(): Boolean {
        return parentDir != null
    }

    private fun goBack() {
        onParentDirectoryClicked()
    }

    private fun readRootPath(): String {
        val rootPath = settings.rootPath
        return when {
            rootPath.isNullOrBlank() -> "/*"
            rootPath.endsWith("/") -> "$rootPath*"
            rootPath.endsWith("/*") -> rootPath
            else -> "$rootPath/*"
        }
    }

    private fun readContentProviderAuthority(): String {
        return settings.contentProviderAuthority
    }

    private fun readAccessToken(): String {
        return settings.accessToken ?: EMPTY
    }

    companion object {
        private const val MIME_TYPE_TEXT = "text/plain"
    }
}