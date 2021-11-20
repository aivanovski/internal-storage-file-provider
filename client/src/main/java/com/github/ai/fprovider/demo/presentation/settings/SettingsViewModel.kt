package com.github.ai.fprovider.demo.presentation.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ai.fprovider.demo.R
import com.github.ai.fprovider.demo.domain.OnSettingsChangeListener
import com.github.ai.fprovider.demo.domain.ResourceProvider
import com.github.ai.fprovider.demo.domain.Settings
import com.github.terrakok.cicerone.Router

class SettingsViewModel(
    private val settings: Settings,
    private val resourceProvider: ResourceProvider,
    private val router: Router
) : ViewModel() {

    val accessTokenDescription = MutableLiveData(formatAccessTokenDescription())
    val rootPathDescription = MutableLiveData(formatRootPathDescription())

    private val settingsListener = OnSettingsChangeListener { onSettingsChanged(it) }

    init {
        settings.registerListener(settingsListener)
    }

    override fun onCleared() {
        settings.unregisterListener(settingsListener)
    }

    fun onBackClicked() {
        router.exit()
    }

    private fun onSettingsChanged(type: Settings.Type) {
        when (type) {
            Settings.Type.ACCESS_TOKEN -> {
                accessTokenDescription.value = formatAccessTokenDescription()
            }
            Settings.Type.ROOT_PATH -> {
                rootPathDescription.value = formatRootPathDescription()
            }
        }
    }

    private fun formatAccessTokenDescription(): String {
        val accessToken = settings.accessToken
        return if (accessToken != null) {
            resourceProvider.getString(R.string.access_token_title, accessToken)
        } else {
            resourceProvider.getString(R.string.not_specified)
        }
    }

    private fun formatRootPathDescription(): String {
        val rootPath = settings.rootPath
        return rootPath ?: resourceProvider.getString(R.string.not_specified)
    }
}