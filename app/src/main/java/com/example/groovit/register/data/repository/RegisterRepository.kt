package com.example.groovit.register.data.repository

import com.example.groovit.core.network.RetrofitHelper
import com.example.groovit.register.data.model.RegisterRequest
import com.example.groovit.register.data.model.RegisterResponse

class RegisterRepository {
    private val registerService = RetrofitHelper.registerService

    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = registerService.register(registerRequest)

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