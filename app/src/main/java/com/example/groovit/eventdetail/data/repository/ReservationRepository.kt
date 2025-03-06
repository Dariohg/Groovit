package com.example.groovit.eventdetail.data.repository

import com.example.groovit.core.network.RetrofitHelper
import com.example.groovit.eventdetail.data.model.ReservationRequest
import com.example.groovit.eventdetail.data.model.ReservationResponse

class ReservationRepository {
    private val service = RetrofitHelper.reservationService

    suspend fun createReservation(request: ReservationRequest): Result<ReservationResponse> {
        return try {
            val response = service.createReservation(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear reserva: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}