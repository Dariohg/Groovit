package com.example.groovit.register.data.datasource

import com.example.groovit.register.data.model.RegisterRequest
import com.example.groovit.register.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("usuarios")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}