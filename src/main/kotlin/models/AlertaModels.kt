package com.mecaniq.models

import kotlinx.serialization.Serializable

enum class TipoRevisao(
    val descricao: String,
    val kmIntervalo: Int,
    val mesesIntervalo: Int
) {
    TROCA_OLEO_FILTRO("Troca de Óleo e Filtro de Óleo", 10_000, 6),
    FILTRO_COMBUSTIVEL("Troca do Filtro de Combustível", 20_000, 12),
    CORREIA_DENTADA("Inspeção/Troca da Correia Dentada", 50_000, 36),
    PASTILHAS_FREIO("Revisão de Pastilhas e Fluido de Freio", 20_000, 12)
}

@Serializable
data class AlertaPreventivoDTO(
    val veiculoId: String,
    val tipo: String,
    val descricao: String,
    val kmUltimaTroca: Int,
    val kmProximaTroca: Int,
    val dataProximaTrocaEstimada: String,
    val kmRestantes: Int,
    val statusAlerta: StatusAlerta
)

enum class StatusAlerta {
    EM_DIA,
    PROXIMO_DO_VENCIMENTO, // Menos de 1.000 km restantes
    VENCIDO
}