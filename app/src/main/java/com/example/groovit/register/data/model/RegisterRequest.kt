package com.example.groovit.register.data.model

data class RegisterRequest(
    val nombre: String,
    val apellido: String,
    val username: String,
    val contraseña: String,
    val email: String,
    val token_dispositivo: String,
    val generosMusicales: List<String>
)