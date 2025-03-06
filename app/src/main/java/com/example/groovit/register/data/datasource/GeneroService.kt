package com.example.groovit.register.data.datasource

import com.example.groovit.register.data.model.GeneroResponse
import retrofit2.Response
import retrofit2.http.GET

interface GeneroService {
    @GET("generos")
    suspend fun getGeneros(): Response<GeneroResponse>
}