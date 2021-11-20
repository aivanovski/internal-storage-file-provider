package com.github.ai.fprovider.demo.domain

import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.annotation.StringRes
import com.github.ai.fprovider.demo.R
import com.github.ai.fprovider.demo.domain.Settings.Type.ACCESS_TOKEN
import com.github.ai.fprovider.demo.domain.Settings.Type.ROOT_PATH
import java.util.concurrent.CopyOnWriteArrayList

class Settings(
    context: Context,
    resourceProvider: ResourceProvider
) {

    var accessToken: String?
        get() = getNonEmptyString(keyAccessToken, null)
        set(value) {
            setString(keyAccessToken, value)
        }

    var rootPath: String?
        get() = getNonEmptyString(keyRootPath, null)
        set(value) {
            setString(keyRootPath, value)
        }

    private val keyAccessToken = ACCESS_TOKEN.getKey(resourceProvider)
    private val keyRootPath = ROOT_PATH.getKey(resourceProvider)
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

    private fun Type.getKey(resourceProvider: ResourceProvider): String {
        return resourceProvider.getString(this.keyResId)
    }

    enum class Type(@StringRes val keyResId: Int) {
        ACCESS_TOKEN(R.string.pref_access_token),
        ROOT_PATH(R.string.pref_root_path)
    }
}