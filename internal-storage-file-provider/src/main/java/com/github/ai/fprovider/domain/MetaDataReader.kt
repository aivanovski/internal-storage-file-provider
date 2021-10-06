package com.github.ai.fprovider.domain

import android.content.Context
import android.content.pm.PackageManager
import com.github.ai.fprovider.MetaData.META_DATA_KEY

class MetaDataReader {

    fun readAuthority(context: Context): String {
        val info = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )

        return info.metaData.getString(META_DATA_KEY)
            ?: throw IllegalStateException(getMessage())
    }

    private fun getMessage(): String {
        return "Failed to find meta-data with key 'com.github.ai.fprovider.authority', " +
            "please add paste it inside <application> tag in AndroidManifest.xml file"
    }
}