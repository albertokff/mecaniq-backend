package com.mecaniq.models

import com.mecaniq.database.tables.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class RegisterOficinaRequest(
    val nomeOficina: String,
    val cnpj: String? = null,
    val telefone: String? = null,
    val nomeAdmin: String,
    val emailAdmin: String,
    val senhaAdmin: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val senha: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val usuarioId: String,
    val nome: String,
    val email: String,
    val role: UserRole,
    val oficinaId: String? = null
)