package com.example.groovit.eventdetail.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class EventDetailResponse(
    val message: String,
    val data: EventDetailModel
)

data class EventDetailModel(
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val capacidad: Int,
    val lugares_disponibles: Int? = null, // Puede ser nulo en la respuesta
    val precio: Double,
    val imagen: String,
    val ubicacion: String,
    val genero: String,
    val id: Int = 0 // Añadido para compatibilidad con la UI
) {
    // Método para obtener lugares disponibles con valor por defecto
    fun getLugaresDisponibles(): Int {
        return lugares_disponibles ?: (capacidad / 2)
    }

    // Método auxiliar para convertir la fecha de String a Date
    fun getFechaAsDate(): Date {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            format.parse(fecha) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }
}