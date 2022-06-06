package com.github.ai.fprovider.client.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.ai.fprovider.client.R
import com.github.ai.fprovider.client.extension.initActionBar
import com.github.ai.fprovider.client.presentation.Screens.FileListScreen
import com.github.ai.fprovider.client.presentation.file_list.FileListArgs
import com.github.ai.fprovider.client.presentation.file_list.model.ProviderData
import com.github.ai.isfprovider.InternalStorageFileProvider.Companion.VIEWER_EXTRA_AUTHORITY
import com.github.ai.isfprovider.InternalStorageFileProvider.Companion.VIEWER_EXTRA_ROOT_PATH
import com.github.ai.isfprovider.InternalStorageFileProvider.Companion.VIEWER_EXTRA_AUTH_TOKEN
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val navigatorHolder: NavigatorHolder by inject()
    private val router: Router by inject()
    private val navigator = AppNavigator(this, R.id.fragmentContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initActionBar(R.id.toolBar)
        router.newRootScreen(FileListScreen(readExtraArgs()))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        router.newRootScreen(FileListScreen(readExtraArgs()))
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val isHandledByFragment =
            supportFragmentManager.fragments.any { it.onOptionsItemSelected(item) }

        return if (isHandledByFragment) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun readExtraArgs(): FileListArgs {
        val extras = intent?.extras?: return FileListArgs()

        val authority = extras.getString(VIEWER_EXTRA_AUTHORITY)
        val rootPath = extras.getString(VIEWER_EXTRA_ROOT_PATH)
        val accessToken = extras.getString(VIEWER_EXTRA_AUTH_TOKEN)

        val data = if (!authority.isNullOrEmpty() &&
            !rootPath.isNullOrEmpty() &&
            !accessToken.isNullOrEmpty()) {
            ProviderData(authority, rootPath, accessToken)
        } else {
            null
        }

        return FileListArgs(providerData = data)
    }
}