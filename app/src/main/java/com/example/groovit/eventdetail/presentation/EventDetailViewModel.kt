package com.example.groovit.eventdetail.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groovit.home.data.model.EventModel
import com.example.groovit.home.data.repository.EventRepository
import kotlinx.coroutines.launch

class EventDetailViewModel : ViewModel() {
    private val repository = EventRepository()

    private val _event = MutableLiveData<EventModel>()
    val event: LiveData<EventModel> = _event

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _ticketCount = MutableLiveData(1)
    val ticketCount: LiveData<Int> = _ticketCount

    private val _totalPrice = MutableLiveData(0.0)
    val totalPrice: LiveData<Double> = _totalPrice

    private val _purchaseSuccess = MutableLiveData(false)
    val purchaseSuccess: LiveData<Boolean> = _purchaseSuccess

    private val _purchaseMessage = MutableLiveData<String?>(null)
    val purchaseMessage: LiveData<String?> = _purchaseMessage

    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Simulamos un pequeño retraso como si fuera una llamada de red
                kotlinx.coroutines.delay(500)

                // Obtenemos el evento específico por ID
                val eventList = repository.getEvents()
                val selectedEvent = eventList.find { it.id == eventId }

                if (selectedEvent != null) {
                    _event.value = selectedEvent
                    updateTotalPrice()
                } else {
                    _error.value = "No se encontró el evento"
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar el evento: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun incrementTicketCount() {
        val currentEvent = _event.value
        val currentCount = _ticketCount.value ?: 1

        if (currentEvent != null && currentCount < currentEvent.lugaresDisponibles) {
            _ticketCount.value = currentCount + 1
            updateTotalPrice()
        }
    }

    fun decrementTicketCount() {
        val currentCount = _ticketCount.value ?: 1

        if (currentCount > 1) {
            _ticketCount.value = currentCount - 1
            updateTotalPrice()
        }
    }

    private fun updateTotalPrice() {
        val currentEvent = _event.value
        val currentCount = _ticketCount.value ?: 1

        if (currentEvent != null) {
            _totalPrice.value = currentEvent.precio * currentCount
        }
    }

    fun purchaseTickets() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _purchaseMessage.value = null

            try {
                val currentEvent = _event.value
                val currentCount = _ticketCount.value ?: 1

                if (currentEvent == null) {
                    _error.value = "No se encontró información del evento"
                    return@launch
                }

                // Simulamos la llamada a la API para comprar entradas
                kotlinx.coroutines.delay(1500)

                // Simulamos una respuesta exitosa
                val purchaseData = mapOf(
                    "eventId" to currentEvent.id,
                    "ticketCount" to currentCount,
                    "totalPrice" to (currentEvent.precio * currentCount)
                )

                // Log de la compra simulada
                println("Compra simulada: $purchaseData")

                _purchaseSuccess.value = true
                _purchaseMessage.value = "¡Compra exitosa! Has adquirido $currentCount ${if (currentCount == 1) "entrada" else "entradas"} para ${currentEvent.titulo}."

            } catch (e: Exception) {
                _error.value = "Error al procesar la compra: ${e.message}"
                _purchaseSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetPurchaseState() {
        _purchaseSuccess.value = false
        _purchaseMessage.value = null
    }
}