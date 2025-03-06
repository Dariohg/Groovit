package com.example.groovit.register.data.model

data class GeneroMusical(
    val id: String,
    val genero: String,
    var isSelected: Boolean = false
)

data class GeneroResponse(
    val generos: List<GeneroMusical>
)