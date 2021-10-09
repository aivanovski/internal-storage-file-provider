package com.github.ai.fprovider.logging

import android.util.Log
import kotlin.reflect.KClass

internal class AndroidLogcatLogger(type: KClass<*>) : Logger {

    private val tag = type.simpleName

    override fun d(message: String) {
        Log.d(tag, message)
    }
}