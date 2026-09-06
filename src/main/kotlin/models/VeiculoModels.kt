package com.mecaniq.models

import kotlinx.serialization.Serializable

@Serializable
data class VeiculoDTO(
    val id: String,
    val clienteId: String,
    val placa: String,
    val modelo: String,
    val marca: String,
    val ano: Int,
    val kmAtual: Int
)

@Serializable
data class CriarVeiculoRequest(
    val clienteId: String,
    val placa: String,
    val modelo: String,
    val marca: String,
    val ano: Int,
    val kmAtual: Int = 0
)

@Serializable
data class AtualizarKmRequest(
    val novoKm: Int
)