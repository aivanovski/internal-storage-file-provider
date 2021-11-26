package com.github.ai.fprovider.client.domain.proxy_provider

import android.net.Uri
import com.github.ai.fprovider.client.R
import com.github.ai.fprovider.client.data.db.dao.ProxyFileReferenceDao
import com.github.ai.fprovider.client.data.db.entity.ProxyFileReference
import com.github.ai.fprovider.client.data.entity.FileEntity
import com.github.ai.fprovider.client.data.entity.FilePath
import com.github.ai.fprovider.client.domain.ResourceProvider
import com.github.ai.fprovider.client.domain.Settings
import com.github.ai.fprovider.client.extension.getCleanPath
import com.github.ai.fprovider.client.extension.toUri
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit

class ProxyProviderInteractor(
    private val dao: ProxyFileReferenceDao,
    private val settings: Settings,
    private val resourceProvider: ResourceProvider
) {

    fun createProxyUri(file: FileEntity): Uri {
        val path = file.getCleanPath()

        dao.getByPath(path)
            .mapNotNull { it.id }
            .forEach { dao.remove(it) }

        val uid = generateUid()

        dao.add(
            ProxyFileReference(
                id = null,
                uid = uid,
                path = path,
                created = Date(System.currentTimeMillis())
            )
        )

        val authority = resourceProvider.getString(R.string.content_provider_authority)
        return Uri.parse("$URI_SCHEME://$authority/$uid")
    }

    fun resolveOriginalUri(proxyUid: String): Uri? {
        val file = dao.getByUid(proxyUid).firstOrNull() ?: return null
        if (file.isExpired()) {
            return null
        }

        val accessToken = settings.accessToken ?: return null

        return FilePath(
            authority = settings.contentProviderAuthority,
            path = file.path,
            accessToken = accessToken
        ).toUri()
    }

    private fun generateUid(): String {
        return (UUID.randomUUID().toString() + UUID.randomUUID().toString())
            .replace("-", "")
    }

    private fun ProxyFileReference.isExpired(): Boolean {
        return System.currentTimeMillis() - created.time >= EXPIRATION_PERIOD
    }

    companion object {
        private val EXPIRATION_PERIOD = TimeUnit.HOURS.toMillis(12)
        private const val URI_SCHEME = "content"
    }
}