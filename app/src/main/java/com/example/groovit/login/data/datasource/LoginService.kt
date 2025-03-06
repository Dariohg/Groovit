package com.example.groovit.login.data.datasource

import com.example.groovit.login.data.model.LoginRequest
import com.example.groovit.login.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("auth/login-admin")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}