package com.github.ai.fprovider.client.di

import android.content.Context
import androidx.room.Room
import com.github.ai.fprovider.client.data.FileSystem
import com.github.ai.fprovider.client.data.FileSystemImpl
import com.github.ai.fprovider.client.data.db.AppDatabase
import com.github.ai.fprovider.client.domain.ErrorInteractor
import com.github.ai.fprovider.client.domain.ResourceProvider
import com.github.ai.fprovider.client.domain.Settings
import com.github.ai.fprovider.client.domain.file_list.FileListInteractor
import com.github.ai.fprovider.client.domain.proxy_provider.ProxyProviderInteractor
import com.github.ai.fprovider.client.presentation.file_list.FileListViewModel
import com.github.ai.fprovider.client.presentation.file_list.cells.FileListCellFactory
import com.github.ai.fprovider.client.presentation.settings.SettingsViewModel
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

        // Database
        single { provideAppDatabase(get()) }
        single { provideProxyFileReferenceDao(get()) }

        // Cicerone
        single { Cicerone.create() }
        single { provideCiceroneRouter(get()) }
        single { provideCiceroneNavigatorHolder(get()) }

        // Proxy provider
        single { ProxyProviderInteractor(get(), get(), get()) }

        // FileList
        single { FileListInteractor(get(), get()) }
        single { FileListCellFactory(get()) }
        viewModel { FileListViewModel(get(), get(), get(), get(), get(), get()) }

        // Settings
        viewModel { SettingsViewModel(get(), get(), get()) }
    }

    private fun provideCiceroneRouter(cicerone: Cicerone<Router>): Router  =
        cicerone.router

    private fun provideCiceroneNavigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder =
        cicerone.getNavigatorHolder()

    private fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "isbrowser-db")
            .build()
    }

    private fun provideProxyFileReferenceDao(db: AppDatabase) =
        db.proxyFileReferenceDao()
}