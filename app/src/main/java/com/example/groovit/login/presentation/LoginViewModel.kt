package com.example.groovit.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groovit.login.domain.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val loginUseCase = LoginUseCase()

    private val _username = MutableLiveData<String>("")
    val username: LiveData<String> = _username

    private val _contraseña = MutableLiveData<String>("")
    val contraseña: LiveData<String> = _contraseña

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoggedIn = MutableLiveData<Boolean>(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun onUsernameChanged(username: String) {
        _username.value = username
    }

    fun onPasswordChanged(password: String) {
        _contraseña.value = password
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = loginUseCase(
                username = _username.value ?: "",
                contraseña = _contraseña.value ?: ""
            )

            _isLoading.value = false

            result.fold(
                onSuccess = {
                    // Aquí guardarías el token en preferencias o datastore
                    _isLoggedIn.value = true
                },
                onFailure = {
                    _errorMessage.value = it.message
                }
            )
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

/*

package com.example.groovit.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _username = MutableLiveData<String>("")
    val username: LiveData<String> = _username

    private val _contraseña = MutableLiveData<String>("")
    val contraseña: LiveData<String> = _contraseña

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoggedIn = MutableLiveData<Boolean>(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun onUsernameChanged(username: String) {
        _username.value = username
    }

    fun onPasswordChanged(password: String) {
        _contraseña.value = password
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // Simulamos una llamada a la API con un delay
            kotlinx.coroutines.delay(1000)

            // Credenciales de prueba o login simulado
            // Puedes usar cualquier credencial o simplemente permitir cualquier combinación
            if (_username.value?.isNotEmpty() == true && _contraseña.value?.isNotEmpty() == true) {
                // Login exitoso simulado
                _isLoggedIn.value = true
            } else {
                // Error simulado
                _errorMessage.value = "Por favor, introduce un nombre de usuario y contraseña válidos"
            }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}*/