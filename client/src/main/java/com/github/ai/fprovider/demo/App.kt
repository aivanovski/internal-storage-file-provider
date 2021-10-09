package com.github.ai.fprovider.demo

import androidx.multidex.MultiDexApplication
import com.github.ai.fprovider.demo.di.KoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(KoinModule.appModule)
        }
    }
}