package com.github.ai.fprovider.client.domain.proxy_provider

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.github.ai.fprovider.client.di.GlobalInjector

class ProxyFileProvider : ContentProvider() {

    private lateinit var contentResolver: ContentResolver
    private val interactor: ProxyProviderInteractor
        get() = GlobalInjector.get()

    override fun onCreate(): Boolean {
        contentResolver = context?.contentResolver ?: return false
        return true
    }

    override fun getType(uri: Uri): String? {
        val proxyUid = uri.getCleanPath() ?: return null
        val originalUri = interactor.resolveOriginalUri(proxyUid) ?: return null
        return contentResolver.getType(originalUri)
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        throw UnsupportedOperationException()
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException()
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException()
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {

        throw UnsupportedOperationException()
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val proxyUid = uri.getCleanPath() ?: return null
        val originalUri = interactor.resolveOriginalUri(proxyUid) ?: return null

        return contentResolver.openFileDescriptor(originalUri, mode)
    }

    private fun Uri.getCleanPath(): String? {
        val path = this.path ?: return null

        return if (path.contains("/")) {
            path.replace("/", "")
        } else {
            path
        }
    }
}