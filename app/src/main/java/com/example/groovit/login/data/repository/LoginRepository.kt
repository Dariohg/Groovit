package com.example.groovit.login.data.repository

import com.example.groovit.core.network.RetrofitHelper
import com.example.groovit.login.data.model.LoginRequest
import com.example.groovit.login.data.model.LoginResponse

class LoginRepository {
    private val loginService = RetrofitHelper.loginService

    suspend fun login(username: String, contraseña: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(username, contraseña)
            println("LOGIN REQUEST: $request")

            val response = loginService.login(request)

            if (response.isSuccessful) {
                println("LOGIN SUCCESS: ${response.body()}")
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                println("LOGIN ERROR: Code ${response.code()}, Error: $errorBody")
                Result.failure(Exception("Error: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            println("LOGIN EXCEPTION: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}