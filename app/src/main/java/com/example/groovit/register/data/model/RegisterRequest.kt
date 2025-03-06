package com.example.groovit.register.data.model

data class RegisterRequest(
    val nombre: String,
    val apellido: String,
    val username: String,
    val email: String,
    val password: String,
    val deviceToken: String,
    val generosMusicales: List<String>
)