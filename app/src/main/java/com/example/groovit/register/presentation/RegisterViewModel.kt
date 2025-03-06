package com.example.groovit.register.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groovit.register.data.model.GeneroMusical
import com.example.groovit.register.data.model.RegisterRequest
import com.example.groovit.register.data.repository.RegisterRepository
import com.example.groovit.register.domain.RegisterUseCase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val repository = RegisterRepository()
    private val registerUseCase = RegisterUseCase() // Añadir esta línea

    private val _nombre = MutableLiveData<String>("")
    val nombre: LiveData<String> = _nombre

    private val _apellido = MutableLiveData<String>("")
    val apellido: LiveData<String> = _apellido

    private val _username = MutableLiveData<String>("")
    val username: LiveData<String> = _username

    private val _email = MutableLiveData<String>("")
    val email: LiveData<String> = _email

    private val _contraseña = MutableLiveData<String>("")
    val contraseña: LiveData<String> = _contraseña

    private val _confirmcontraseña = MutableLiveData<String>("")
    val confirmcontraseña: LiveData<String> = _confirmcontraseña

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isRegistered = MutableLiveData<Boolean>(false)
    val isRegistered: LiveData<Boolean> = _isRegistered

    private val _token_dispositivo = MutableLiveData<String>()
    val token_dispositivo: LiveData<String> = _token_dispositivo

    // Géneros musicales
    private val _generos = MutableLiveData<List<GeneroMusical>>(emptyList())
    val generos: LiveData<List<GeneroMusical>> = _generos

    private val _isLoadingGeneros = MutableLiveData<Boolean>(false)
    val isLoadingGeneros: LiveData<Boolean> = _isLoadingGeneros

    init {
        // Cargar géneros musicales al inicializar
        loadGeneros()
        getDeviceToken()
    }

    private fun loadGeneros() {
        viewModelScope.launch {
            _isLoadingGeneros.value = true

            try {
                val generosResult = repository.getGeneros()

                if (generosResult.isSuccess) {
                    _generos.value = generosResult.getOrNull() ?: emptyList()
                } else {
                    // Si falla, usar una lista de respaldo
                    _generos.value = listOf(
                        GeneroMusical("1", "Rock"),
                        GeneroMusical("2", "Pop"),
                        GeneroMusical("3", "Hip Hop"),
                        GeneroMusical("4", "Electrónica"),
                        GeneroMusical("5", "Reggaeton"),
                        GeneroMusical("6", "Jazz"),
                        GeneroMusical("7", "Clásica")
                    )
                }
            } catch (e: Exception) {
                // Si ocurre una excepción no manejada, usar lista de respaldo
                _generos.value = listOf(
                    GeneroMusical("1", "Rock"),
                    GeneroMusical("2", "Pop"),
                    GeneroMusical("3", "Hip Hop"),
                    GeneroMusical("4", "Electrónica"),
                    GeneroMusical("5", "Reggaeton")
                )
            } finally {
                _isLoadingGeneros.value = false
            }
        }
    }

    fun onNombreChanged(value: String) {
        _nombre.value = value
    }

    fun onApellidoChanged(value: String) {
        _apellido.value = value
    }

    fun onUsernameChanged(value: String) {
        _username.value = value
    }

    fun onEmailChanged(value: String) {
        _email.value = value
    }

    fun onPasswordChanged(value: String) {
        _contraseña.value = value
    }

    fun onConfirmPasswordChanged(value: String) {
        _confirmcontraseña.value = value
    }

    // Función para alternar la selección de un género
    fun toggleGeneroSelection(genero: GeneroMusical) {
        val currentGeneros = _generos.value?.toMutableList() ?: mutableListOf()
        val index = currentGeneros.indexOfFirst { it.id == genero.id }

        if (index != -1) {
            currentGeneros[index] = currentGeneros[index].copy(isSelected = !currentGeneros[index].isSelected)
            _generos.value = currentGeneros
        }
    }

    // Obtener géneros seleccionados
    private fun getSelectedGeneros(): List<String> {
        return _generos.value?.filter { it.isSelected }?.map { it.id } ?: emptyList()
    }

    fun onRegisterClicked() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // En un caso real, esta sería la forma de obtener el token del dispositivo para notificaciones push
            val token_dispositivo = _token_dispositivo.value ?: ""

            // Obtener los géneros seleccionados
            val generosIds = getSelectedGeneros()

            try {
                val result = registerUseCase(
                    nombre = _nombre.value ?: "",
                    apellido = _apellido.value ?: "",
                    username = _username.value ?: "",
                    contraseña = _contraseña.value ?: "",
                    confirmcontraseña = _confirmcontraseña.value ?: "",
                    email = _email.value ?: "",
                    token_dispositivo = token_dispositivo,
                    generosMusicales = generosIds
                )

                if (result.isSuccess) {
                    _isRegistered.value = true

                    suscribeAEventos()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Error desconocido"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _token_dispositivo.value = task.result
                Log.d("Token", "Token: ${task.result}")
            } else {
                Log.e("Token", "Error al obtener el token: ${task.exception}")
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun suscribeAEventos() {
        FirebaseMessaging.getInstance().subscribeToTopic("eventos")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Kottili", "Usuario suscrito exitosamente al topic evento")
                } else {
                    Log.e("Kottili", "Error al suscribir usuario al topic evento: ${task.exception}")
                }
            }
    }
}