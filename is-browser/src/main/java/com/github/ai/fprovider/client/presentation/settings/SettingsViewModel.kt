package com.github.ai.fprovider.client.presentation.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ai.fprovider.client.R
import com.github.ai.fprovider.client.domain.OnSettingsChangeListener
import com.github.ai.fprovider.client.domain.ResourceProvider
import com.github.ai.fprovider.client.domain.Settings
import com.github.terrakok.cicerone.Router

class SettingsViewModel(
    private val settings: Settings,
    private val resourceProvider: ResourceProvider,
    private val router: Router
) : ViewModel() {

    val accessTokenDescription = MutableLiveData(formatAccessTokenDescription())
    val rootPathDescription = MutableLiveData(formatRootPathDescription())
    val authorityDescription = MutableLiveData(formatAuthorityDescription())

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
            Settings.Type.CONTENT_PROVIDER_AUTHORITY -> {
                authorityDescription.value = formatAuthorityDescription()
            }
        }
    }

    private fun formatAccessTokenDescription(): String {
        val accessToken = settings.accessToken
        return if (accessToken != null) {
            resourceProvider.getString(R.string.access_token_summary, accessToken)
        } else {
            resourceProvider.getString(R.string.not_specified)
        }
    }

    private fun formatRootPathDescription(): String {
        val rootPath = settings.rootPath
        return rootPath ?: resourceProvider.getString(R.string.not_specified)
    }

    private fun formatAuthorityDescription(): String {
        val authority = settings.contentProviderAuthority
        return if (authority.isBlank()) {
            resourceProvider.getString(R.string.not_specified)
        } else {
            authority
        }
    }
}