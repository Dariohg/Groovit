package com.example.groovit.login.domain

import com.example.groovit.login.data.model.LoginResponse
import com.example.groovit.login.data.repository.LoginRepository

class LoginUseCase {
    private val repository = LoginRepository()

    suspend operator fun invoke(username: String, password: String): Result<LoginResponse> {
        if (username.isBlank()) {
            return Result.failure(Exception("El nombre de usuario no puede estar vacío"))
        }

        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }

        return repository.login(username, password)
    }
}