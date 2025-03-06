package com.example.groovit.eventdetail.data.datasource

import com.example.groovit.eventdetail.data.model.ReservationRequest
import com.example.groovit.eventdetail.data.model.ReservationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReservationService {
    @POST("reservas")
    suspend fun createReservation(@Body request: ReservationRequest): Response<ReservationResponse>
}