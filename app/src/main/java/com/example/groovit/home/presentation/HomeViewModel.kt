package com.example.groovit.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groovit.home.data.model.EventModel
import com.example.groovit.home.domain.GetEventsUseCase
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val getEventsUseCase = GetEventsUseCase()

    private val _events = MutableLiveData<List<EventModel>>()
    val events: LiveData<List<EventModel>> = _events

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Simulamos un pequeño retraso como si fuera una llamada de red
                kotlinx.coroutines.delay(800)

                // Obtenemos los eventos
                val eventsList = getEventsUseCase()
                _events.value = eventsList
            } catch (e: Exception) {
                _error.value = "Error al cargar los eventos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}