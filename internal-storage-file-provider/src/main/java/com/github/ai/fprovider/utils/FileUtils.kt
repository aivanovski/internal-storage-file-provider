package com.github.ai.fprovider.utils

import com.github.ai.fprovider.utils.Constants.EMPTY
import java.io.File

internal object FileUtils {

    internal fun concatenatePath(vararg pathes: String): String {
        if (pathes.isEmpty()) {
            return EMPTY
        }

        val result = StringBuilder()
        for (path in pathes) {
            val cleanPath = if (path.endsWith(File.separatorChar)) {
                path.substring(0, path.lastIndexOf(File.separatorChar))
            } else {
                path
            }

            if (cleanPath.isEmpty()) {
                continue
            }

            if (result.isNotEmpty()) {
                if (cleanPath.startsWith(File.separatorChar)) {
                    result.append(cleanPath)
                } else {
                    result.append(File.separatorChar).append(cleanPath)
                }
            } else {
                result.append(cleanPath)
            }
        }

        return result.toString()
    }

    internal fun getExtensionFromName(name: String): String? {
        val lastPointIdx = name.lastIndexOf('.')
        if (lastPointIdx < 0 || lastPointIdx == name.length - 1) {
            return null
        }

        return name.substring(lastPointIdx + 1)
    }
}