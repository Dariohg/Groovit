package com.example.groovit.home.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Añade esta clase para manejar la respuesta de la API
data class EventResponse(
    val message: String,
    val data: List<EventModel>
)

data class EventModel(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val capacidad: Int,
    val lugares_disponibles: Int,
    val precio: Double,
    val imagen: String,
    val ubicacion: String,
    val genero: String
) {
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