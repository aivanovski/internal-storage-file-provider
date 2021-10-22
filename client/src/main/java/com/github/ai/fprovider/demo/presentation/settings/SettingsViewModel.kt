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
        }
    }

    private fun formatAccessTokenDescription(): String {
        val accessToken = settings.accessToken
        return if (accessToken != null) {
            resourceProvider.getString(R.string.current_value_with_str, accessToken)
        } else {
            resourceProvider.getString(R.string.not_specified)
        }
    }
}