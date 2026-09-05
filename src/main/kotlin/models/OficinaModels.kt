package com.mecaniq.models

import kotlinx.serialization.Serializable

@Serializable
data class OficinaDTO(
    val id: String,
    val nome: String,
    val cnpj: String? = null,
    val telefone: String,
    val email: String? = null
)

@Serializable
data class CriarOficinaRequest(
    val nome: String,
    val cnpj: String? = null,
    val telefone: String,
    val email: String? = null
)