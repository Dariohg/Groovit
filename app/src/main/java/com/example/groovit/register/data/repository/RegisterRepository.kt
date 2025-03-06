package com.example.groovit.register.data.repository

import com.example.groovit.core.network.RetrofitHelper
import com.example.groovit.register.data.model.GeneroMusical
import com.example.groovit.register.data.model.RegisterRequest
import com.example.groovit.register.data.model.RegisterResponse

class RegisterRepository {
    private val registerService = RetrofitHelper.registerService
    private val generoService = RetrofitHelper.generoService

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

    suspend fun getGeneros(): Result<List<GeneroMusical>> {
        return try {
            val response = generoService.getGeneros()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.generos)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}