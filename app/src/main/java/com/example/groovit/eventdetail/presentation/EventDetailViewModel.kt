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

    // Cambiado a 0 como valor inicial
    private val _ticketCount = MutableLiveData<Int>(0)
    val ticketCount: LiveData<Int> = _ticketCount

    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double> = _totalPrice

    private val _purchaseSuccess = MutableLiveData<Boolean>(false)
    val purchaseSuccess: LiveData<Boolean> = _purchaseSuccess

    private val _purchaseMessage = MutableLiveData<String?>(null)
    val purchaseMessage: LiveData<String?> = _purchaseMessage

    private var _currentTicketCount = 0


    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                println("Intentando cargar evento con ID: $eventId")

                val eventList = repository.getEvents()
                println("Eventos recuperados: ${eventList.size}")

                val selectedEvent = eventList.find { it.id == eventId }

                if (selectedEvent != null) {
                    println("Evento encontrado: ${selectedEvent.titulo}")
                    _event.value = selectedEvent
                    _currentTicketCount = 0
                    _ticketCount.value = 0
                    _totalPrice.value = 0.0
                    println("Contador reiniciado a 0")
                } else {
                    println("No se encontró el evento con ID: $eventId")
                    _error.value = "No se encontró el evento"
                }
            } catch (e: Exception) {
                println("Error al cargar evento: ${e.message}")
                _error.value = "Error al cargar el evento: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun incrementTicketCount() {
        val currentEvent = _event.value

        println("Intentando incrementar desde: $_currentTicketCount")

        if (currentEvent != null && _currentTicketCount < currentEvent.lugaresDisponibles) {
            _currentTicketCount++
            _ticketCount.postValue(_currentTicketCount) // Usar postValue para asegurar actualización
            println("Incrementado a: $_currentTicketCount")

            // Actualizar precio directamente
            val newPrice = currentEvent.precio * _currentTicketCount
            _totalPrice.postValue(newPrice)
            println("Precio actualizado a: $newPrice")
        }
    }

    fun decrementTicketCount() {
        println("Intentando decrementar desde: $_currentTicketCount")

        if (_currentTicketCount > 0) {
            _currentTicketCount--
            _ticketCount.postValue(_currentTicketCount) // Usar postValue para asegurar actualización
            println("Decrementado a: $_currentTicketCount")

            // Actualizar precio directamente
            val currentEvent = _event.value
            if (currentEvent != null) {
                val newPrice = currentEvent.precio * _currentTicketCount
                _totalPrice.postValue(newPrice)
                println("Precio actualizado a: $newPrice")
            }
        }
    }

    private fun updateTotalPrice() {
        val currentEvent = _event.value
        val currentCount = _ticketCount.value ?: 0

        if (currentEvent != null) {
            val newPrice = currentEvent.precio * currentCount
            println("Actualizando precio: $currentCount tickets * ${currentEvent.precio} = $newPrice")
            _totalPrice.value = newPrice
        }
    }

    fun purchaseTickets() {
        viewModelScope.launch {
            val currentCount = _ticketCount.value ?: 0

            // Verificar que hay tickets seleccionados
            if (currentCount <= 0) {
                _error.value = "Debes seleccionar al menos un boleto"
                return@launch
            }

            _isLoading.value = true
            _error.value = null
            _purchaseMessage.value = null

            try {
                val currentEvent = _event.value

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