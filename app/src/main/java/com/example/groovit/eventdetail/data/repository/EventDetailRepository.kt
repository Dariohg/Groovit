package com.example.groovit.eventdetail.data.repository

import com.example.groovit.core.network.RetrofitHelper
import com.example.groovit.eventdetail.data.model.EventDetailModel

class EventDetailRepository {
    private val service = RetrofitHelper.eventDetailService

    suspend fun getEventDetail(eventId: String): EventDetailModel {
        try {
            val response = service.getEventDetail(eventId)

            if (response.isSuccessful && response.body() != null) {
                // Añadir información de debugging
                println("Respuesta API exitosa para evento $eventId: ${response.body()}")

                // Asignar lugares_disponibles si no viene en la respuesta
                val eventData = response.body()!!.data

                // Para compatibilidad con la UI, necesitamos asegurarnos que tengamos un valor para lugares_disponibles
                // y asignar el ID del evento desde la URL
                return eventData.copy(
                    id = eventId.toIntOrNull() ?: 0,
                    lugares_disponibles = eventData.lugares_disponibles ?: (eventData.capacidad / 2) // valor por defecto
                )
            } else {
                println("Error en la respuesta para evento $eventId: ${response.code()} - ${response.message()}")
                throw Exception("Error al cargar detalles del evento: ${response.message()}")
            }
        } catch (e: Exception) {
            println("Excepción al obtener detalles del evento $eventId: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}