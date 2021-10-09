package com.github.ai.fprovider.demo.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.ai.fprovider.demo.presentation.file_list.FileListFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    class FileListScreen : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            FileListFragment.newInstance()
    }
}