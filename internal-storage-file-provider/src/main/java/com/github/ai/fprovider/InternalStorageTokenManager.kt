package com.github.ai.fprovider

import android.content.Context

class InternalStorageTokenManager(context: Context) {

    private val preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    fun addToken(token: String) {
        val allTokens = readAll()
        save(allTokens + token)
    }

    fun removeAllTokens() {
        save(emptySet())
    }

    internal fun isTokenValid(token: String): Boolean {
        return readAll().contains(token)
    }

    private fun readAll(): Set<String> {
        return preferences.getStringSet(KEY_ALL_TOKENS, null) ?: emptySet()
    }

    private fun save(tokens: Set<String>) {
        preferences.edit().apply {
            putStringSet(KEY_ALL_TOKENS, tokens)
            apply()
        }
    }

    companion object {
        internal const val KEY_ALL_TOKENS = "allTokens"
        internal const val SHARED_PREFS_NAME = "internal-storage-file-provider"
    }
}