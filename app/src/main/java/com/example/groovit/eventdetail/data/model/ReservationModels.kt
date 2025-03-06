package com.example.groovit.eventdetail.data.model

data class ReservationRequest(
    val cantidad_lugares: Int,
    val total: Double,
    val evento_id: Int,
    val usuario_id: Int,  // Obtendremos este valor del token de autenticación
    val token: String     // Token del dispositivo, no de sesión
)

data class ReservationResponse(
    val success: Boolean,
    val message: String,
    val data: ReservationData?
)

data class ReservationData(
    val id: Int,
    val fecha: String,
    val estado: String
    // Otros campos que pueda devolver la API
)