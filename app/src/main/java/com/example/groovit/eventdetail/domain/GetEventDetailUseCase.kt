package com.example.groovit.eventdetail.domain

import com.example.groovit.eventdetail.data.model.EventDetailModel
import com.example.groovit.eventdetail.data.repository.EventDetailRepository

class GetEventDetailUseCase {
    private val repository = EventDetailRepository()

    suspend operator fun invoke(eventId: String): EventDetailModel {
        return repository.getEventDetail(eventId)
    }
}