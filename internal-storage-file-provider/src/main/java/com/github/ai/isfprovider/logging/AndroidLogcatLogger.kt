package com.github.ai.isfprovider.logging

import android.util.Log
import kotlin.reflect.KClass

internal class AndroidLogcatLogger(type: KClass<*>) : Logger {

    private val tag = type.simpleName

    override fun d(message: String) {
        Log.d(tag, message)
    }

    override fun e(message: String) {
        Log.e(tag, message)
    }
}