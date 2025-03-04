package com.example.groovit.register.data.model

data class RegisterResponse(
    val id: String,
    val username: String,
    val token: String,
    val success: Boolean,
    val message: String?
)