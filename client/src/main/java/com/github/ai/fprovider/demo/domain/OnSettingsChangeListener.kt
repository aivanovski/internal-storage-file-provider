package com.github.ai.fprovider.demo.domain

fun interface OnSettingsChangeListener {
    fun onSettingsChanged(type: Settings.Type)
}