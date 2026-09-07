package com.mecaniq.services

import com.mecaniq.models.AlertaPreventivoDTO
import com.mecaniq.models.StatusAlerta
import com.mecaniq.models.TipoRevisao
import com.mecaniq.repositories.VeiculoRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AlertaPreventivoService(
    private val veiculoRepository: VeiculoRepository
) {

    suspend fun calcularAlertas(veiculoId: String): List<AlertaPreventivoDTO> {
        val veiculo = veiculoRepository.buscarPorId(veiculoId)
            ?: throw IllegalArgumentException("Veículo não encontrado")

        val kmAtual = veiculo.kmAtual
        val dataHoje = LocalDate.now()
        val formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        // Calcula as revisões com base no odômetro atual
        return TipoRevisao.entries.map { tipo ->
            val kmProximaTroca = kmAtual + tipo.kmIntervalo
            val dataProxima = dataHoje.plusMonths(tipo.mesesIntervalo.toLong())
            val kmRestantes = kmProximaTroca - kmAtual

            val status = when {
                kmRestantes <= 0 -> StatusAlerta.VENCIDO
                kmRestantes <= 1000 -> StatusAlerta.PROXIMO_DO_VENCIMENTO
                else -> StatusAlerta.EM_DIA
            }

            AlertaPreventivoDTO(
                veiculoId = veiculoId,
                tipo = tipo.name,
                descricao = tipo.descricao,
                kmUltimaTroca = kmAtual,
                kmProximaTroca = kmProximaTroca,
                dataProximaTrocaEstimada = dataProxima.format(formatador),
                kmRestantes = kmRestantes,
                statusAlerta = status
            )
        }
    }
}