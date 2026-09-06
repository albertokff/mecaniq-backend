package com.mecaniq.repositories

import com.mecaniq.backend.database.DatabaseFactory.dbQuery
import com.mecaniq.database.VeiculosTable
import com.mecaniq.models.AtualizarKmRequest
import com.mecaniq.models.CriarVeiculoRequest
import com.mecaniq.models.VeiculoDTO
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.util.UUID

class VeiculoRepository {

    suspend fun listarPorCliente(clienteId: String): List<VeiculoDTO> = dbQuery {
        VeiculosTable.selectAll()
            .where { VeiculosTable.clienteId eq UUID.fromString(clienteId) }
            .map { it.toVeiculoDTO() }
    }

    suspend fun buscarPorPlaca(placa: String): VeiculoDTO? = dbQuery {
        VeiculosTable.selectAll()
            .where { VeiculosTable.placa eq placa.uppercase().trim() }
            .map { it.toVeiculoDTO() }
            .singleOrNull()
    }

    suspend fun inserir(request: CriarVeiculoRequest): String = dbQuery {
        val novoId = VeiculosTable.insertAndGetId {
            it[clienteId] = UUID.fromString(request.clienteId)
            it[placa] = request.placa.uppercase().trim()
            it[modelo] = request.modelo
            it[marca] = request.marca
            it[ano] = request.ano
            it[kmAtual] = request.kmAtual
        }
        novoId.value.toString()
    }

    suspend fun atualizarKm(veiculoId: String, request: AtualizarKmRequest): Boolean = dbQuery {
        val linhasAfetadas = VeiculosTable.update({ VeiculosTable.id eq UUID.fromString(veiculoId) }) {
            it[kmAtual] = request.novoKm
        }
        linhasAfetadas > 0
    }

    // Função de extensão privada para mapeamento limpo de ResultRow -> DTO
    private fun org.jetbrains.exposed.sql.ResultRow.toVeiculoDTO() = VeiculoDTO(
        id = this[VeiculosTable.id].value.toString(),
        clienteId = this[VeiculosTable.clienteId].value.toString(),
        placa = this[VeiculosTable.placa],
        modelo = this[VeiculosTable.modelo],
        marca = this[VeiculosTable.marca],
        ano = this[VeiculosTable.ano],
        kmAtual = this[VeiculosTable.kmAtual]
    )
}