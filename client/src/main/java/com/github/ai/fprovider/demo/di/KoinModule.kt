package com.github.ai.fprovider.demo.di

import com.github.ai.fprovider.demo.data.FileSystem
import com.github.ai.fprovider.demo.data.FileSystemImpl
import com.github.ai.fprovider.demo.domain.ErrorInteractor
import com.github.ai.fprovider.demo.domain.ResourceProvider
import com.github.ai.fprovider.demo.domain.Settings
import com.github.ai.fprovider.demo.domain.file_list.FileListInteractor
import com.github.ai.fprovider.demo.presentation.file_list.FileListViewModel
import com.github.ai.fprovider.demo.presentation.file_list.cells.FileListCellFactory
import com.github.ai.fprovider.demo.presentation.settings.SettingsViewModel
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModule {

    val appModule = module {
        single { ResourceProvider(get()) }
        single { ErrorInteractor(get()) }
        single<FileSystem> { FileSystemImpl(get()) }
        single { Settings(get(), get()) }

        // Cicerone
        single { Cicerone.create() }
        single { provideCiceroneRouter(get()) }
        single { provideCiceroneNavigatorHolder(get()) }

        // FileList
        single { FileListInteractor(get()) }
        single { FileListCellFactory(get()) }
        viewModel { FileListViewModel(get(), get(), get(), get(), get(), get()) }

        // Settings
        viewModel { SettingsViewModel(get(), get(), get()) }
    }

    private fun provideCiceroneRouter(cicerone: Cicerone<Router>): Router  =
        cicerone.router

    private fun provideCiceroneNavigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder =
        cicerone.getNavigatorHolder()
}