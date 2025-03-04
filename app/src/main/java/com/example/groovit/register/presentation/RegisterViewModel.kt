package com.example.groovit.register.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groovit.register.domain.RegisterUseCase
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val registerUseCase = RegisterUseCase()

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

    fun onRegisterClicked() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // En un caso real, esta sería la forma de obtener el token del dispositivo para notificaciones push
            val deviceToken = getDeviceToken()

            val result = registerUseCase(
                nombre = _nombre.value ?: "",
                apellido = _apellido.value ?: "",
                username = _username.value ?: "",
                email = _email.value ?: "",
                password = _password.value ?: "",
                confirmPassword = _confirmPassword.value ?: "",
                deviceToken = deviceToken
            )

            _isLoading.value = false

            result.fold(
                onSuccess = {
                    _isRegistered.value = true
                },
                onFailure = {
                    _errorMessage.value = it.message
                }
            )
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