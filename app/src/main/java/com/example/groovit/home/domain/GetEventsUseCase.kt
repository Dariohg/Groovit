package com.example.groovit.home.domain

import com.example.groovit.home.data.model.EventModel
import com.example.groovit.home.data.repository.EventRepository

class GetEventsUseCase {
    private val repository = EventRepository()

    operator fun invoke(): List<EventModel> {
        return repository.getEvents()
    }
}