package com.example.groovit.home.data.datasource

import com.example.groovit.home.data.model.EventResponse
import retrofit2.Response
import retrofit2.http.GET

interface EventService {
    @GET("eventos")
    suspend fun getEvents(): Response<EventResponse>
}