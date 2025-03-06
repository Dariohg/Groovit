package com.example.groovit.eventdetail.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groovit.eventdetail.data.model.EventDetailModel
import com.example.groovit.eventdetail.domain.GetEventDetailUseCase
import com.example.groovit.eventdetail.domain.PurchaseTicketsUseCase
import kotlinx.coroutines.launch

class EventDetailViewModel : ViewModel() {
    private val getEventDetailUseCase = GetEventDetailUseCase()

    private val _event = MutableLiveData<EventDetailModel?>()
    val event: LiveData<EventDetailModel?> = _event

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _ticketCount = MutableLiveData<Int>(0)
    val ticketCount: LiveData<Int> = _ticketCount

    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double> = _totalPrice

    private val _purchaseSuccess = MutableLiveData<Boolean>(false)
    val purchaseSuccess: LiveData<Boolean> = _purchaseSuccess

    private val _purchaseMessage = MutableLiveData<String?>(null)
    val purchaseMessage: LiveData<String?> = _purchaseMessage

    private var _currentTicketCount = 0

    private val purchaseTicketsUseCase = PurchaseTicketsUseCase()


    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                println("Cargando evento con ID: $eventId")

                val eventDetail = getEventDetailUseCase(eventId)
                println("Evento recibido en ViewModel: $eventDetail")

                _event.value = eventDetail  // Usar value en vez de postValue para actualización inmediata
                _currentTicketCount = 0
                _ticketCount.value = 0
                _totalPrice.value = 0.0

                println("Evento cargado correctamente: ${eventDetail.titulo}")
            } catch (e: Exception) {
                println("Error al cargar evento: ${e.message}")
                e.printStackTrace()
                _error.value = "Error al cargar el evento: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun incrementTicketCount() {
        val currentEvent = _event.value

        println("Intentando incrementar tickets. Evento: $currentEvent, Count: $_currentTicketCount")

        if (currentEvent != null && _currentTicketCount < currentEvent.getLugaresDisponibles()) {
            _currentTicketCount++
            _ticketCount.value = _currentTicketCount
            println("Incrementado a: $_currentTicketCount")

            // Actualizar precio
            val newPrice = currentEvent.precio * _currentTicketCount
            _totalPrice.value = newPrice
            println("Precio actualizado a: $newPrice")
        }
    }

    fun decrementTicketCount() {
        println("Intentando decrementar desde: $_currentTicketCount")

        if (_currentTicketCount > 0) {
            _currentTicketCount--
            _ticketCount.value = _currentTicketCount
            println("Decrementado a: $_currentTicketCount")

            // Actualizar precio
            val currentEvent = _event.value
            if (currentEvent != null) {
                val newPrice = currentEvent.precio * _currentTicketCount
                _totalPrice.value = newPrice
                println("Precio actualizado a: $newPrice")
            }
        }
    }

    fun purchaseTickets() {
        viewModelScope.launch {
            val currentCount = _ticketCount.value ?: 0
            val currentEvent = _event.value

            // Verificar que hay tickets seleccionados
            if (currentCount <= 0) {
                _error.value = "Debes seleccionar al menos un boleto"
                return@launch
            }

            // Verificar que hay un evento cargado
            if (currentEvent == null) {
                _error.value = "No se encontró información del evento"
                return@launch
            }

            _isLoading.value = true
            _error.value = null
            _purchaseMessage.value = null

            try {
                // Calcular el precio total
                val totalPrice = currentEvent.precio * currentCount

                // Realizar la compra
                val result = purchaseTicketsUseCase.invoke(
                    eventId = currentEvent.id,
                    quantity = currentCount,
                    totalPrice = totalPrice
                )

                result.fold(
                    onSuccess = { response ->
                        _purchaseSuccess.value = true
                        _purchaseMessage.value = response.message ?:
                                "¡Compra exitosa! Has adquirido $currentCount ${if (currentCount == 1) "entrada" else "entradas"} para ${currentEvent.titulo}."
                    },
                    onFailure = { exception ->
                        _error.value = "Error al procesar la compra: ${exception.message}"
                        _purchaseSuccess.value = false
                    }
                )
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