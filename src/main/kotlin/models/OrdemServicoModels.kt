package com.mecaniq.models

import com.mecaniq.database.StatusOS
import com.mecaniq.database.TipoItem
import kotlinx.serialization.Serializable

@Serializable
data class ItemOSRequest(
    val descricao: String,
    val tipo: TipoItem,
    val quantidade: Int,
    val precoUnitario: Double // Recebe via JSON e convertemos para BigDecimal internamente
)

@Serializable
data class CriarOSRequest(
    val oficinaId: String,
    val veiculoId: String,
    val kmEntrada: Int,
    val observacoes: String? = null,
    val itens: List<ItemOSRequest> = emptyList()
)

@Serializable
data class ItemOSDTO(
    val id: String,
    val descricao: String,
    val tipo: TipoItem,
    val quantidade: Int,
    val precoUnitario: Double
)

@Serializable
data class OrdemServicoDTO(
    val id: String,
    val oficinaId: String,
    val veiculoId: String,
    val status: StatusOS,
    val kmEntrada: Int,
    val valorTotal: Double,
    val observacoes: String?,
    val itens: List<ItemOSDTO>
)