package com.example.groovit.register.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groovit.register.data.model.GeneroMusical
import com.example.groovit.register.data.model.RegisterRequest
import com.example.groovit.register.data.repository.RegisterRepository
import com.example.groovit.register.domain.RegisterUseCase
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

    private val _password = MutableLiveData<String>("")
    val password: LiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>("")
    val confirmPassword: LiveData<String> = _confirmPassword

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isRegistered = MutableLiveData<Boolean>(false)
    val isRegistered: LiveData<Boolean> = _isRegistered

    // Géneros musicales
    private val _generos = MutableLiveData<List<GeneroMusical>>(emptyList())
    val generos: LiveData<List<GeneroMusical>> = _generos

    private val _isLoadingGeneros = MutableLiveData<Boolean>(false)
    val isLoadingGeneros: LiveData<Boolean> = _isLoadingGeneros

    init {
        // Cargar géneros musicales al inicializar
        loadGeneros()
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
        _password.value = value
    }

    fun onConfirmPasswordChanged(value: String) {
        _confirmPassword.value = value
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
            val deviceToken = getDeviceToken()

            // Obtener los géneros seleccionados
            val generosIds = getSelectedGeneros()

            try {
                val result = registerUseCase(
                    nombre = _nombre.value ?: "",
                    apellido = _apellido.value ?: "",
                    username = _username.value ?: "",
                    email = _email.value ?: "",
                    password = _password.value ?: "",
                    confirmPassword = _confirmPassword.value ?: "",
                    deviceToken = deviceToken,
                    generosMusicales = generosIds
                )

                if (result.isSuccess) {
                    _isRegistered.value = true
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

    private fun getDeviceToken(): String {
        // Aquí iría la lógica para obtener el token del dispositivo para notificaciones push
        // Por ejemplo, usando Firebase Messaging o servicios similares
        // Para simplificar, retornamos un valor dummy
        return "device_token_dummy"
    }

    fun clearError() {
        _errorMessage.value = null
    }
}