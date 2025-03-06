package com.example.groovit.eventdetail.data.datasource

import com.example.groovit.eventdetail.data.model.EventDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EventDetailService {
    @GET("eventos/{id}")
    suspend fun getEventDetail(@Path("id") eventId: String): Response<EventDetailResponse>
}