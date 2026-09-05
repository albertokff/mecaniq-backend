package com.mecaniq.models

import kotlinx.serialization.Serializable

@Serializable
data class ClienteDTO(
    val id: String? = null,
    val oficinaId: String,
    val nome: String,
    val telefone: String,
    val email: String? = null,
    val cpf: String? = null
)

@Serializable
data class CriarClienteRequest(
    val oficinaId: String,
    val nome: String,
    val telefone: String,
    val email: String? = null,
    val cpf: String? = null
)