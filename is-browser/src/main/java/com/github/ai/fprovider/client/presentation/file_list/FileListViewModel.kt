package com.github.ai.fprovider.client.presentation.file_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ai.fprovider.client.R
import com.github.ai.fprovider.client.data.entity.FileEntity
import com.github.ai.fprovider.client.data.entity.FilePath
import com.github.ai.fprovider.client.domain.ErrorInteractor
import com.github.ai.fprovider.client.domain.OnSettingsChangeListener
import com.github.ai.fprovider.client.domain.ResourceProvider
import com.github.ai.fprovider.client.domain.Settings
import com.github.ai.fprovider.client.domain.file_list.FileListInteractor
import com.github.ai.fprovider.client.extension.toFilePath
import com.github.ai.fprovider.client.presentation.Screens.SettingsScreen
import com.github.ai.fprovider.client.presentation.core.cells.BaseCellViewModel
import com.github.ai.fprovider.client.presentation.core.model.ScreenState
import com.github.ai.fprovider.client.presentation.core.model.ScreenState.Companion
import com.github.ai.fprovider.client.presentation.core.model.ScreenStateType.DATA
import com.github.ai.fprovider.client.presentation.core.model.ScreenStateType.DATA_WITH_ERROR
import com.github.ai.fprovider.client.presentation.file_list.cells.FileListCellModelFactory
import com.github.ai.fprovider.client.presentation.file_list.cells.FileListCellViewModelFactory
import com.github.ai.fprovider.client.presentation.file_list.model.FileDialogModel
import com.github.ai.fprovider.client.presentation.file_list.model.OpenFileModel
import com.github.ai.fprovider.client.presentation.file_list.model.ProviderData
import com.github.ai.fprovider.client.utils.Event
import com.github.ai.fprovider.client.utils.MimeTypes
import com.github.ai.fprovider.client.utils.StringUtils.EMPTY
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch

class FileListViewModel(
    private val interactor: FileListInteractor,
    private val modelFactory: FileListCellModelFactory,
    private val viewModelFactory: FileListCellViewModelFactory,
    private val errorInteractor: ErrorInteractor,
    private val resourceProvider: ResourceProvider,
    private val router: Router,
    private val settings: Settings
) : ViewModel() {

    val screenState = MutableLiveData(ScreenState.loading())
    val cellViewModels = MutableLiveData<List<BaseCellViewModel>>()
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
    private var args: FileListArgs? = null
    private var accessToken: String? = null
    private var handledProviderData: ProviderData? = null
    private var isShowSaveDataQuestion: Boolean = false
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

    fun start(args: FileListArgs?) {
        this.args = args

        if (args?.providerData != null && args.providerData != handledProviderData) {
            setRootPath(
                FilePath(
                    authority = args.providerData.authority,
                    path = args.providerData.rootPath,
                    accessToken = args.providerData.accessToken
                )
            )
            accessToken = args.providerData.accessToken
            handledProviderData = args.providerData

            isShowSaveDataQuestion = true
        } else {
            accessToken = null
            isShowSaveDataQuestion = false
        }

        loadData()
    }

    private fun loadData() {
        if (!isProviderDataSpecified() && args?.providerData == null) {
            screenState.value = ScreenState.empty(
                resourceProvider.getString(R.string.content_provider_access_is_not_configured)
            )
            return
        }

        screenState.value = ScreenState.loading()
        viewModelScope.launch {
            if (currentDir == null) {
                val getCurrentDir = interactor.getFile(
                    currentPath.toFilePath(
                        accessToken = accessToken ?: readAccessToken()
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

                val cellModels = modelFactory.createCellModels(
                    parent = parentDir,
                    files = files,
                    accessToken = accessToken ?: readAccessToken(),
                    isShowHiddenFiles = settings.isShowHiddenFiles,
                    isShowSaveDataQuestion = isShowSaveDataQuestion,
                    onSaveDataCancelled = { onSaveDataCancelled() },
                    onSaveDataConfirmed = { onSaveDataConfirmed() },
                    onFileClicked = { file -> onFileClicked(file) },
                    onFileLongClicked = { file -> onFileLongClicked(file) }
                )

                cellViewModels.value = viewModelFactory.createViewModels(cellModels)

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
        openFile(file, isOpenAsText = false)
    }

    fun onOpenFileAsTextClicked(file: FileEntity) {
        openFile(file, isOpenAsText = true)
    }

    private fun onSaveDataCancelled() {
        isShowSaveDataQuestion = false

        loadData()
    }

    private fun onSaveDataConfirmed() {
        val data = handledProviderData ?: return

        settings.contentProviderAuthority = data.authority
        settings.rootPath = data.rootPath
        settings.accessToken = data.accessToken

        accessToken = null

        isShowSaveDataQuestion = false

        loadData()
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
                openFile(file, isOpenAsText = false)
            }
        }
    }

    private fun openFile(file: FileEntity, isOpenAsText: Boolean) {
        val mimeType = if (isOpenAsText) {
            MimeTypes.TEXT
        } else {
            getMimeTypeForFile(file)
        }

        viewModelScope.launch {
            val uriResult = interactor.createProxyUri(file)

            if (uriResult.isSuccess) {
                openFileEvent.value = Event(
                    OpenFileModel(
                        uri = uriResult.getOrThrow(),
                        mimeType = mimeType
                    )
                )
            } else {
                showError(uriResult.getExceptionOrThrow())
            }
        }
    }

    private fun onFileLongClicked(file: FileEntity) {
        showFileDialogEvent.value = FileDialogModel(file)
    }

    private fun getMimeTypeForFile(file: FileEntity): String {
        return if (file.mimeType.isNullOrBlank()) {
            MimeTypes.TEXT
        } else {
            file.mimeType
        }
    }

    private fun onParentDirectoryClicked() {
        if (parents.size >= 2) {
            val last = parents.last()
            val beforeLast = parents[parents.size - 2]

            onDirectoryOpened(last, beforeLast)
        } else if (parents.size > 0) {
            val last = parents.last()

            onDirectoryOpened(last, null)
        }

        if (parents.isNotEmpty()) {
            parents.removeAt(parents.size - 1)
        }

        loadData()
    }

    private fun onDirectoryClicked(dir: FileEntity) {
        val currentDir = this.currentDir ?: return

        parents.add(currentDir)
        onDirectoryOpened(dir, currentDir)

        loadData()
    }

    private fun onDirectoryOpened(currentDir: FileEntity, parentDir: FileEntity?) {
        this.currentDir = currentDir
        this.parentDir = parentDir

        currentPath = currentDir.toFilePath(accessToken = accessToken ?: readAccessToken())
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
            Settings.Type.IS_SHOW_HIDDEN_FILES -> {
                loadData()
            }
        }
    }

    private fun setRootPath(path: FilePath) {
        currentPath = path
        currentDir = null
        parentDir = null
        parents.clear()
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

    private fun isProviderDataSpecified(): Boolean {
        val authority = settings.contentProviderAuthority
        val rootPath = settings.rootPath ?: EMPTY
        val accessToken = settings.accessToken ?: EMPTY
        return authority.isNotEmpty() && rootPath.isNotEmpty() && accessToken.isNotEmpty()
    }
}