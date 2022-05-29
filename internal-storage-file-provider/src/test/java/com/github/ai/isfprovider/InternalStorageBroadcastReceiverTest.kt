package com.github.ai.isfprovider

import android.content.Context
import android.content.Intent
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.COMMAND_ADD_TOKEN
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.COMMAND_LAUNCH_VIEWER
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.COMMAND_REMOVE_ALL_TOKENS
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.ERROR_ARGUMENT_IS_NOT_SPECIFIED
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.ERROR_FAILED_TO_ADD_TOKEN
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.ERROR_INCORRECT_COMMAND
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.EXTRA_AUTHORITY
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.EXTRA_COMMAND
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.EXTRA_PATH
import com.github.ai.isfprovider.InternalStorageBroadcastReceiver.Companion.EXTRA_AUTH_TOKEN
import com.github.ai.isfprovider.domain.ActivityLauncher
import com.github.ai.isfprovider.logging.Logger
import com.github.ai.isfprovider.test.TestData.AUTHORITY
import com.github.ai.isfprovider.test.TestData.AUTH_TOKEN
import com.github.ai.isfprovider.test.TestData.DIRECTORY_FILE
import com.github.ai.isfprovider.utils.Constants.EMPTY
import com.google.common.truth.Truth.assertThat
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class InternalStorageBroadcastReceiverTest {

    private val tokenManager: InternalStorageTokenManager = mockk()
    private val logger: Logger = mockk(relaxUnitFun = true)
    private val activityLauncher: ActivityLauncher = mockk()
    private val context: Context = mockk()
    private val broadcastReceiver = InternalStorageBroadcastReceiver(
        tokenManager = tokenManager,
        logger = logger,
        activityLauncher = activityLauncher
    )

    @Test
    fun `onReceive should print error to log if command is not specified`() {
        // arrange
        val intent = Intent()

        // act
        broadcastReceiver.onReceive(context, intent)

        // assert
        verify { logger.e(ERROR_INCORRECT_COMMAND) }
        confirmVerified(logger)
    }

    @Test
    fun `onReceive should print error to log if command is incorrect`() {
        // arrange
        val intent = Intent()
            .apply {
                putExtra(EXTRA_COMMAND, EMPTY)
            }

        // act
        broadcastReceiver.onReceive(context, intent)

        // assert
        verify { logger.e(ERROR_INCORRECT_COMMAND) }
        confirmVerified(logger)
    }

    @Test
    fun `onReceive should print error to log if token is not specified for add token command`() {
        // arrange
        val intent = Intent()
            .apply {
                putExtra(EXTRA_COMMAND, COMMAND_ADD_TOKEN)
                putExtra(EXTRA_PATH, DIRECTORY_FILE.path)
            }
        val error = String.format(ERROR_ARGUMENT_IS_NOT_SPECIFIED, EXTRA_AUTH_TOKEN)

        // act
        broadcastReceiver.onReceive(context, intent)

        // assert
        verify { logger.e(error) }
        confirmVerified(logger)
    }

    @Test
    fun `onReceive should print error to log if path is not specified for add token command`() {
        // arrange
        val intent = Intent()
            .apply {
                putExtra(EXTRA_COMMAND, COMMAND_ADD_TOKEN)
                putExtra(EXTRA_AUTH_TOKEN, AUTH_TOKEN)
            }
        val error = String.format(ERROR_ARGUMENT_IS_NOT_SPECIFIED, EXTRA_PATH)

        // act
        broadcastReceiver.onReceive(context, intent)

        // assert
        verify { logger.e(error) }
        confirmVerified(logger)
    }

    @Test
    fun `onReceive should send token and path to TokenManager`() {
        // arrange
        val intent = Intent()
            .apply {
                putExtra(EXTRA_COMMAND, COMMAND_ADD_TOKEN)
                putExtra(EXTRA_AUTH_TOKEN, AUTH_TOKEN)
                putExtra(EXTRA_PATH, DIRECTORY_FILE.path)
            }

        every { tokenManager.addToken(AUTH_TOKEN, DIRECTORY_FILE.path) }.returns(Unit)

        // act
        broadcastReceiver.onReceive(context, intent)

        // assert
        verify { tokenManager.addToken(AUTH_TOKEN, DIRECTORY_FILE.path) }
        confirmVerified(tokenManager)
    }

    @Test
    fun `onReceive should print error to log if TokenManager throws exception`() {
        // arrange
        val intent = Intent()
            .apply {
                putExtra(EXTRA_COMMAND, COMMAND_ADD_TOKEN)
                putExtra(EXTRA_AUTH_TOKEN, AUTH_TOKEN)
                putExtra(EXTRA_PATH, DIRECTORY_FILE.path)
            }

        every { tokenManager.addToken(AUTH_TOKEN, DIRECTORY_FILE.path) }.throws(
            IllegalArgumentException()
        )

        // act
        broadcastReceiver.onReceive(context, intent)

        // assert
        verify { tokenManager.addToken(AUTH_TOKEN, DIRECTORY_FILE.path) }
        verify { logger.e(ERROR_FAILED_TO_ADD_TOKEN) }
        confirmVerified(tokenManager, logger)
    }

    @Test
    fun `onReceive should remove all tokens`() {
        // arrange
        every { tokenManager.removeAllTokens() }.returns(Unit)
        val intent = Intent()
            .apply {
                putExtra(EXTRA_COMMAND, COMMAND_REMOVE_ALL_TOKENS)
            }

        // act
        broadcastReceiver.onReceive(context, intent)

        // assert
        verify { tokenManager.removeAllTokens() }
        confirmVerified(tokenManager)
    }

    @Test
    fun `onReceive should print error to log if path is not specified for launch command`() {
        // arrange
        val intent = Intent()
            .apply {
                putExtra(EXTRA_COMMAND, COMMAND_LAUNCH_VIEWER)
                putExtra(EXTRA_AUTH_TOKEN, AUTH_TOKEN)
                putExtra(EXTRA_AUTHORITY, AUTHORITY)
            }
        val error = String.format(ERROR_ARGUMENT_IS_NOT_SPECIFIED, EXTRA_PATH)

        // act
        broadcastReceiver.onReceive(context, intent)

        // assert
        verify { logger.e(error) }
        confirmVerified(logger)
    }

    @Test
    fun `onReceive should print error to log if token is not specified for launch command`() {
        // arrange
        val intent = Intent()
            .apply {
                putExtra(EXTRA_COMMAND, COMMAND_LAUNCH_VIEWER)
                putExtra(EXTRA_PATH, DIRECTORY_FILE.path)
                putExtra(EXTRA_AUTHORITY, AUTHORITY)
            }
        val error = String.format(ERROR_ARGUMENT_IS_NOT_SPECIFIED, EXTRA_AUTH_TOKEN)

        // act
        broadcastReceiver.onReceive(context, intent)

        // assert
        verify { logger.e(error) }
        confirmVerified(logger)
    }

    @Test
    fun `onReceive should print error to log if authority is not specified for launch command`() {
        // arrange
        val intent = Intent()
            .apply {
                putExtra(EXTRA_COMMAND, COMMAND_LAUNCH_VIEWER)
                putExtra(EXTRA_PATH, DIRECTORY_FILE.path)
                putExtra(EXTRA_AUTH_TOKEN, AUTH_TOKEN)
            }
        val error = String.format(ERROR_ARGUMENT_IS_NOT_SPECIFIED, EXTRA_AUTHORITY)

        // act
        broadcastReceiver.onReceive(context, intent)

        // assert
        verify { logger.e(error) }
        confirmVerified(logger)
    }

    @Test
    fun `onReceive should start viewer activity`() {
        // arrange
        val intent = Intent()
            .apply {
                putExtra(EXTRA_COMMAND, COMMAND_LAUNCH_VIEWER)
                putExtra(EXTRA_AUTHORITY, AUTHORITY)
                putExtra(EXTRA_PATH, DIRECTORY_FILE.path)
                putExtra(EXTRA_AUTH_TOKEN, AUTH_TOKEN)
            }

        val capturedViewerIntent = slot<Intent>()
        every { activityLauncher.launchSafely(any(), capture(capturedViewerIntent)) }.returns(Unit)
        every { context.packageName }.returns(PACKAGE_NAME)

        // act
        broadcastReceiver.onReceive(context, intent)

        // assert
        val viewerIntent = capturedViewerIntent.captured

        assertThat(viewerIntent.component?.packageName)
            .isEqualTo(PACKAGE_NAME)
        assertThat(viewerIntent.component?.className)
            .isEqualTo(InternalStorageBroadcastReceiver::class.java.name)
        assertThat(viewerIntent.extras?.getString(InternalStorageFileProvider.VIEWER_EXTRA_AUTHORITY))
            .isEqualTo(AUTHORITY)
        assertThat(viewerIntent.extras?.getString(InternalStorageFileProvider.VIEWER_EXTRA_ROOT_PATH))
            .isEqualTo(DIRECTORY_FILE.path)
        assertThat(viewerIntent.extras?.getString(InternalStorageFileProvider.VIEWER_EXTRA_AUTH_TOKEN))
            .isEqualTo(AUTH_TOKEN)
    }

    companion object {
        private const val PACKAGE_NAME = "package_name"
    }
}