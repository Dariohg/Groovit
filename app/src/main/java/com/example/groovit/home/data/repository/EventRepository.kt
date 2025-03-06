package com.example.groovit.home.data.repository

import com.example.groovit.core.network.RetrofitHelper
import com.example.groovit.home.data.model.EventModel

class EventRepository {
    private val eventService = RetrofitHelper.eventService

    suspend fun getEvents(): List<EventModel> {
        return try {
            val response = eventService.getEvents()

            if (response.isSuccessful && response.body() != null) {
                response.body()!!.data
            } else {
                throw Exception("Error al cargar eventos: ${response.message()}")
            }
        } catch (e: Exception) {
            // Registrar el error y relanzar la excepción
            println("Error al obtener eventos: ${e.message}")
            throw e
        }
    }
}