package com.github.ai.fprovider.client.utils

import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

object StringUtils {

    const val EMPTY = ""

    private val FILE_SIZE_UNITS = listOf("B", "KB", "MB", "GB", "TB")

    fun formatFileSize(size: Long): String {
        if (size <= 0) {
            return "0 B"
        }

        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#")
            .format(size / 1024.0.pow(digitGroups.toDouble()))
            .toString() + " " + FILE_SIZE_UNITS[digitGroups]
    }
}