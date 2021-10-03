package com.github.ai.fprovider.demo.ext

import java.io.File

fun String.getFileNameFromPath(): String {
    val idx = this.lastIndexOf(File.separatorChar)
    return if (idx >= 0 && idx < this.length - 1) {
        this.substring(idx + 1)
    } else if (idx == 0 && this.length == 1) {
        this
    } else {
        ""
    }
}