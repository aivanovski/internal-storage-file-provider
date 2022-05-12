package com.github.ai.fprovider.client.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.ai.fprovider.client.presentation.file_list.FileListArgs
import com.github.ai.fprovider.client.presentation.file_list.FileListFragment
import com.github.ai.fprovider.client.presentation.settings.SettingsFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    class FileListScreen(private val args: FileListArgs) : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            FileListFragment.newInstance(args)
    }

    class SettingsScreen : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            SettingsFragment.newInstance()
    }
}