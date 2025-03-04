package com.example.groovit.home.data.repository

import com.example.groovit.home.data.model.EventModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventRepository {

    // Esta función simula obtener eventos de una API
    // En el futuro, aquí se conectará con el backend real
    fun getEvents(): List<EventModel> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        return listOf(
            EventModel(
                id = "1",
                titulo = "Festival Groove Urbano",
                descripcion = "El festival más esperado del año con los mejores artistas de música urbana. Una experiencia única con 3 escenarios y más de 15 horas de música continua.",
                fecha = dateFormat.parse("15/08/2025 18:00") ?: Date(),
                capacidadTotal = 5000,
                lugaresDisponibles = 2345,
                precio = 1200.0,
                ubicacion = "Parque Bicentenario, CDMX",
                imagenUrl = "https://us.123rf.com/450wm/singpentinkhappy/singpentinkhappy2009/singpentinkhappy200905128/154875764-diseño-del-festival-de-música-para-la-fiesta-y-el-evento-de-año-nuevo-2021-para-banner-e-impresión.jpg?ver=6"
            ),
            EventModel(
                id = "2",
                titulo = "Concierto Sinfónico Rock",
                descripcion = "Una fusión única entre la música clásica y el rock. La orquesta sinfónica interpretará los grandes éxitos del rock de todos los tiempos.",
                fecha = dateFormat.parse("22/09/2025 20:30") ?: Date(),
                capacidadTotal = 2000,
                lugaresDisponibles = 856,
                precio = 850.0,
                ubicacion = "Auditorio Nacional, CDMX",
                imagenUrl = "https://us.123rf.com/450wm/singpentinkhappy/singpentinkhappy2009/singpentinkhappy200905128/154875764-diseño-del-festival-de-música-para-la-fiesta-y-el-evento-de-año-nuevo-2021-para-banner-e-impresión.jpg?ver=6"
            ),
            EventModel(
                id = "3",
                titulo = "Electro Beach Party",
                descripcion = "La mejor música electrónica a la orilla del mar. DJ's internacionales, shows de luces y una experiencia inmersiva única en la playa.",
                fecha = dateFormat.parse("05/10/2025 16:00") ?: Date(),
                capacidadTotal = 3500,
                lugaresDisponibles = 1200,
                precio = 1500.0,
                ubicacion = "Playa del Carmen, Quintana Roo",
                imagenUrl = "https://us.123rf.com/450wm/singpentinkhappy/singpentinkhappy2009/singpentinkhappy200905128/154875764-diseño-del-festival-de-música-para-la-fiesta-y-el-evento-de-año-nuevo-2021-para-banner-e-impresión.jpg?ver=6"
            )
        )
    }
}