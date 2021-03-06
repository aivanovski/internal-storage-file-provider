package com.github.ai.fprovider.client.presentation.settings

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.github.ai.fprovider.client.R
import com.github.ai.fprovider.client.domain.Settings
import com.github.ai.fprovider.client.extension.setupActionBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModel()

    private lateinit var accessTokenPref: EditTextPreference
    private lateinit var rootPathPref: EditTextPreference
    private lateinit var authorityPref: EditTextPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar {
            title = getString(R.string.settings)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(null)
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = getString(R.string.preferences_name)
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                viewModel.onBackClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accessTokenPref = findPreference(getString(Settings.Type.ACCESS_TOKEN.keyResId))
            ?: throw IllegalStateException()

        rootPathPref = findPreference(getString(Settings.Type.ROOT_PATH.keyResId))
            ?: throw IllegalStateException()

        authorityPref = findPreference(getString(Settings.Type.CONTENT_PROVIDER_AUTHORITY.keyResId))
            ?: throw IllegalStateException()

        viewModel.accessTokenDescription.observe(viewLifecycleOwner) {
            accessTokenPref.summary = it
        }
        viewModel.rootPathDescription.observe(viewLifecycleOwner) {
            rootPathPref.summary = it
        }
        viewModel.authorityDescription.observe(viewLifecycleOwner) {
            authorityPref.summary = it
        }
    }

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}