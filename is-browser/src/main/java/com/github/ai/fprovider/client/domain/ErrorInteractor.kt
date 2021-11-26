package com.github.ai.fprovider.client.domain

import java.lang.Exception

class ErrorInteractor(
    private val resourceProvider: ResourceProvider
) {

    fun getMessage(exception: Exception): String {
        return exception.toString()
    }
}