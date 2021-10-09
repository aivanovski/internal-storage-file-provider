package com.github.ai.fprovider.demo.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.ai.fprovider.demo.R
import com.github.ai.fprovider.demo.extension.initActionBar
import com.github.ai.fprovider.demo.presentation.Screens.FileListScreen
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
        router.newRootScreen(FileListScreen())
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
}