package com.example.groovit.home.data.model

import java.util.Date

data class EventModel(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val fecha: Date,
    val capacidadTotal: Int,
    val lugaresDisponibles: Int,
    val precio: Double,
    val ubicacion: String,
    val imagenUrl: String
)