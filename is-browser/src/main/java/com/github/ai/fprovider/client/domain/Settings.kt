package com.github.ai.fprovider.client.domain

import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.annotation.StringRes
import com.github.ai.fprovider.client.R
import com.github.ai.fprovider.client.domain.Settings.Type.ACCESS_TOKEN
import com.github.ai.fprovider.client.domain.Settings.Type.CONTENT_PROVIDER_AUTHORITY
import com.github.ai.fprovider.client.domain.Settings.Type.IS_SHOW_HIDDEN_FILES
import com.github.ai.fprovider.client.domain.Settings.Type.ROOT_PATH
import com.github.ai.fprovider.client.utils.StringUtils.EMPTY
import java.util.concurrent.CopyOnWriteArrayList

class Settings(
    context: Context,
    resourceProvider: ResourceProvider
) {

    var accessToken: String?
        get() = getNonEmptyString(keyAccessToken, ACCESS_TOKEN.defaultValue)
        set(value) {
            setString(keyAccessToken, value)
        }

    var rootPath: String?
        get() = getNonEmptyString(keyRootPath, ROOT_PATH.defaultValue)
        set(value) {
            setString(keyRootPath, value)
        }

    var contentProviderAuthority: String
        get() = getNonEmptyString(
            keyContentProviderAuthority,
            CONTENT_PROVIDER_AUTHORITY.defaultValue
        ) ?: EMPTY
        set(value) {
            setString(keyContentProviderAuthority, value)
        }

    var isShowHiddenFiles: Boolean
        get() = getBoolean(keyIsShowHiddenFiles, IS_SHOW_HIDDEN_FILES.defaultValue.toBoolean())
        set(value) {
            setBoolean(keyIsShowHiddenFiles, value)
        }

    private val keyAccessToken = ACCESS_TOKEN.getKey(resourceProvider)
    private val keyRootPath = ROOT_PATH.getKey(resourceProvider)
    private val keyContentProviderAuthority = CONTENT_PROVIDER_AUTHORITY.getKey(resourceProvider)
    private val keyIsShowHiddenFiles = IS_SHOW_HIDDEN_FILES.getKey(resourceProvider)

    private val listeners = CopyOnWriteArrayList<OnSettingsChangeListener>()
    private val preferencesListener = OnSharedPreferenceChangeListener { _, key ->
        onPreferenceChanged(key)
    }

    private val keyToTypeMap = Type.values()
        .associateBy { it.getKey(resourceProvider) }

    private val preferences = context.getSharedPreferences(
        resourceProvider.getString(R.string.preferences_name),
        Context.MODE_PRIVATE
    )

    init {
        preferences.registerOnSharedPreferenceChangeListener(preferencesListener)
    }

    fun registerListener(listener: OnSettingsChangeListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun unregisterListener(listener: OnSettingsChangeListener) {
        listeners.remove(listener)
    }

    private fun onPreferenceChanged(key: String) {
        val type = keyToTypeMap[key] ?: return

        listeners.forEach { listener ->
            listener.onSettingsChanged(type)
        }
    }

    private fun getNonEmptyString(key: String, default: String?): String? {
        return preferences.getString(key, default)
            .takeIf { !it.isNullOrEmpty() }
    }

    private fun setString(key: String, value: String?) {
        preferences.edit()
            .apply {
                putString(key, value)
                apply()
            }
    }

    private fun getBoolean(key: String, default: Boolean): Boolean {
        return preferences.getBoolean(key, default)
    }

    private fun setBoolean(key: String, value: Boolean) {
        preferences.edit()
            .apply {
                putBoolean(key, value)
                apply()
            }
    }

    private fun Type.getKey(resourceProvider: ResourceProvider): String {
        return resourceProvider.getString(this.keyResId)
    }

    enum class Type(
        @StringRes val keyResId: Int,
        val defaultValue: String?
    ) {
        ACCESS_TOKEN(
            R.string.pref_access_token,
            null
        ),
        ROOT_PATH(
            R.string.pref_root_path,
            null
        ),
        CONTENT_PROVIDER_AUTHORITY(
            R.string.pref_content_provider_authority,
            "com.termux.internalfiles"
        ),
        IS_SHOW_HIDDEN_FILES(
            R.string.pref_is_show_hidden_files,
            "false"
        )
    }
}