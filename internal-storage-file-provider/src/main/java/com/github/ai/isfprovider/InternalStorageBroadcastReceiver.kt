package com.github.ai.isfprovider

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.ai.isfprovider.domain.ActivityLauncher
import com.github.ai.isfprovider.logging.AndroidLogcatLogger
import com.github.ai.isfprovider.logging.Logger

class InternalStorageBroadcastReceiver(
    private var tokenManager: InternalStorageTokenManager?,
    private val activityLauncher: ActivityLauncher,
    private val logger: Logger
) : BroadcastReceiver() {

    constructor() : this(
        null,
        ActivityLauncher(),
        AndroidLogcatLogger(InternalStorageBroadcastReceiver::class)
    )

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras ?: Bundle.EMPTY

        when (extras.getString(EXTRA_COMMAND)) {
            COMMAND_ADD_TOKEN -> {
                handleAddTokenCommand(context, extras)
            }
            COMMAND_REMOVE_ALL_TOKENS -> {
                handleRemoveAllTokensCommand(context)
            }
            COMMAND_LAUNCH_VIEWER -> {
                handleLaunchViewerCommand(context, extras)
            }
            else -> {
                logger.e(ERROR_INCORRECT_COMMAND)
            }
        }
    }

    private fun handleAddTokenCommand(context: Context, extras: Bundle) {
        val path = extras.getString(EXTRA_PATH)
        if (path == null) {
            logger.e(String.format(ERROR_ARGUMENT_IS_NOT_SPECIFIED, EXTRA_PATH))
            return
        }

        val token = extras.getString(EXTRA_AUTH_TOKEN)
        if (token == null) {
            logger.e(String.format(ERROR_ARGUMENT_IS_NOT_SPECIFIED, EXTRA_AUTH_TOKEN))
            return
        }

        try {
            obtainTokenManager(context).addToken(token, path)
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
            logger.e(ERROR_FAILED_TO_ADD_TOKEN)
        }
    }

    private fun handleRemoveAllTokensCommand(context: Context) {
        obtainTokenManager(context).removeAllTokens()
    }

    private fun handleLaunchViewerCommand(context: Context, extras: Bundle) {
        val authority = extras.getString(EXTRA_AUTHORITY)
        if (authority == null) {
            logger.e(String.format(ERROR_ARGUMENT_IS_NOT_SPECIFIED, EXTRA_AUTHORITY))
            return
        }

        val path = extras.getString(EXTRA_PATH)
        if (path == null) {
            logger.e(String.format(ERROR_ARGUMENT_IS_NOT_SPECIFIED, EXTRA_PATH))
            return
        }

        val token = extras.getString(EXTRA_AUTH_TOKEN)
        if (token == null) {
            logger.e(String.format(ERROR_ARGUMENT_IS_NOT_SPECIFIED, EXTRA_AUTH_TOKEN))
            return
        }

        val intent = Intent()
            .apply {
                action = InternalStorageFileProvider.LAUNCH_VIEWER_INTENT_ACTION
                putExtra(InternalStorageFileProvider.VIEWER_EXTRA_AUTHORITY, authority)
                putExtra(InternalStorageFileProvider.VIEWER_EXTRA_ROOT_PATH, path)
                putExtra(InternalStorageFileProvider.VIEWER_EXTRA_AUTH_TOKEN, token)
            }

        activityLauncher.launchSafely(context, intent)
    }

    private fun obtainTokenManager(context: Context): InternalStorageTokenManager {
        return tokenManager
            ?: InternalStorageTokenManager.from(context)
                .also {
                    tokenManager = it
                }
    }

    companion object {

        const val EXTRA_COMMAND = "com.github.ai.isfprovider.command"
        const val EXTRA_AUTH_TOKEN = "com.github.ai.isfprovider.authToken"
        const val EXTRA_PATH = "com.github.ai.isfprovider.path"
        const val EXTRA_AUTHORITY = "com.github.ai.isfprovider.authority"

        const val COMMAND_ADD_TOKEN = "com.github.ai.isfprovider.ADD_TOKEN"
        const val COMMAND_LAUNCH_VIEWER = "com.github.ai.isfprovider.LAUNCH_VIEWER"
        const val COMMAND_REMOVE_ALL_TOKENS = "com.github.ai.isfprovider.REMOVE_ALL_TOKENS"

        internal const val ERROR_INCORRECT_COMMAND = "Incorrect command"
        internal const val ERROR_ARGUMENT_IS_NOT_SPECIFIED = "Argument is not specified: %s"
        internal const val ERROR_FAILED_TO_ADD_TOKEN = "Failed to add token"
    }
}