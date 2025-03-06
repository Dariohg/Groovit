// eventdetail/domain/PurchaseTicketsUseCase.kt
package com.example.groovit.eventdetail.domain

import android.util.Log
import com.example.groovit.core.auth.AuthManager
import com.example.groovit.eventdetail.data.model.ReservationRequest
import com.example.groovit.eventdetail.data.model.ReservationResponse
import com.example.groovit.eventdetail.data.repository.ReservationRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PurchaseTicketsUseCase {
    private val repository = ReservationRepository()

    suspend fun invoke(
        eventId: Int,
        quantity: Int,
        totalPrice: Double
    ): Result<ReservationResponse> {
        // Verificar que hay un usuario autenticado
        if (!AuthManager.isLoggedIn()) {
            return Result.failure(Exception("Usuario no autenticado"))
        }

        // Obtener el ID del usuario desde AuthManager
        val userId = AuthManager.getUserId()
            ?: return Result.failure(Exception("ID de usuario no disponible"))

        // Obtener el token del dispositivo
        val deviceToken = getDeviceToken()
            ?: return Result.failure(Exception("No se pudo obtener el token del dispositivo"))

        Log.d("PurchaseTickets", "Creando reserva: EventoID=$eventId, UserID=$userId, Cantidad=$quantity, Total=$totalPrice")
        Log.d("PurchaseTickets", "Token de dispositivo: $deviceToken")

        val request = ReservationRequest(
            cantidad_lugares = quantity,
            total = totalPrice,
            evento_id = eventId,
            usuario_id = userId,
            token = deviceToken
        )

        return repository.createReservation(request)
    }

    private suspend fun getDeviceToken(): String? {
        return suspendCancellableCoroutine { continuation ->
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { token ->
                    Log.d("PurchaseTickets", "Token obtenido: $token")
                    continuation.resume(token)
                }
                .addOnFailureListener { exception ->
                    Log.e("PurchaseTickets", "Error al obtener token: ${exception.message}")
                    continuation.resumeWithException(exception)
                }
        }
    }
}