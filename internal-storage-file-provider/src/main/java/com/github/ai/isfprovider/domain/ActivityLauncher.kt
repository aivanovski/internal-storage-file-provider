package com.github.ai.isfprovider.domain

import android.content.Context
import android.content.Intent
import com.github.ai.isfprovider.utils.Constants.EMPTY

class ActivityLauncher {

    fun launchSafely(context: Context, intent: Intent) {
        context.startActivity(Intent.createChooser(intent, EMPTY))
    }
}