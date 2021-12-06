package com.github.ai.isfprovider.domain

class AuthTokenValidator {

    fun isTokenValid(token: String): Boolean {
        return AUTH_TOKEN_PATTERN.matcher(token).matches()
    }

    companion object {
        private val AUTH_TOKEN_PATTERN = "[\\w\\-]{4,256}".toPattern()
    }
}