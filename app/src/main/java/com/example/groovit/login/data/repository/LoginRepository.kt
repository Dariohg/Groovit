package com.example.groovit.login.data.repository

import com.example.groovit.core.network.RetrofitHelper
import com.example.groovit.login.data.model.LoginRequest
import com.example.groovit.login.data.model.LoginResponse

class LoginRepository {
    private val loginService = RetrofitHelper.loginService

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(username, password)
            val response = loginService.login(request)

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}