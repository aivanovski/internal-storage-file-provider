package com.github.ai.fprovider.client.domain

fun interface OnSettingsChangeListener {
    fun onSettingsChanged(type: Settings.Type)
}