package com.example.groovit.register.domain

import com.example.groovit.register.data.model.RegisterRequest
import com.example.groovit.register.data.model.RegisterResponse
import com.example.groovit.register.data.repository.RegisterRepository
import java.util.regex.Pattern

class RegisterUseCase {
    private val repository = RegisterRepository()

    suspend operator fun invoke(
        nombre: String,
        apellido: String,
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        deviceToken: String,
        generosMusicales: List<String> // Añadido este parámetro
    ): Result<RegisterResponse> {
        // Validaciones
        if (nombre.isBlank()) {
            return Result.failure(Exception("El nombre no puede estar vacío"))
        }

        if (apellido.isBlank()) {
            return Result.failure(Exception("El apellido no puede estar vacío"))
        }

        if (username.isBlank() || username.length < 4) {
            return Result.failure(Exception("El nombre de usuario debe tener al menos 4 caracteres"))
        }

        if (!isValidEmail(email)) {
            return Result.failure(Exception("El correo electrónico no es válido"))
        }

        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }

        if (password != confirmPassword) {
            return Result.failure(Exception("Las contraseñas no coinciden"))
        }

        val request = RegisterRequest(
            nombre = nombre,
            apellido = apellido,
            username = username,
            email = email,
            password = password,
            deviceToken = deviceToken,
            generosMusicales = generosMusicales // Incluido en la request
        )

        return repository.register(request)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return emailPattern.matcher(email).matches()
    }
}