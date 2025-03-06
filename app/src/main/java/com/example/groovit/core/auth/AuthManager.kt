package com.example.groovit.core.auth

object AuthManager {
    private var authToken: String? = null

    fun setToken(token: String) {
        authToken = token
    }

    fun getToken(): String? {
        return authToken
    }

    fun clearToken() {
        authToken = null
    }

    fun isLoggedIn(): Boolean {
        return !authToken.isNullOrEmpty()
    }
}