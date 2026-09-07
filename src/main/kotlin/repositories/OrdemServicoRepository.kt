package com.mecaniq.repositories

import com.mecaniq.backend.database.DatabaseFactory.dbQuery
import com.mecaniq.database.ItensOSTable
import com.mecaniq.database.OrdensServicoTable
import com.mecaniq.database.StatusOS
import com.mecaniq.database.VeiculosTable
import com.mecaniq.models.CriarOSRequest
import com.mecaniq.models.ItemOSDTO
import com.mecaniq.models.OrdemServicoDTO
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.math.BigDecimal
import java.util.UUID

class OrdemServicoRepository {
    suspend fun criarOS(request: CriarOSRequest): String = dbQuery {
        VeiculosTable.update({ VeiculosTable.id eq UUID.fromString(request.veiculoId) }) {
            it[kmAtual] = request.kmEntrada
        }

        val totalCalculado = request.itens.fold(BigDecimal.ZERO) { acc, item ->
            val subTotal = BigDecimal.valueOf(item.precoUnitario)
                .multiply(BigDecimal.valueOf(item.quantidade.toLong()))
            acc.add(subTotal)
        }

        val novaOSId = OrdensServicoTable.insertAndGetId {
            it[oficinaId] = UUID.fromString(request.oficinaId)
            it[veiculoId] = UUID.fromString(request.veiculoId)
            it[status] = StatusOS.ABERTA
            it[kmEntrada] = request.kmEntrada
            it[valorTotal] = totalCalculado
            it[observacoes] = request.observacoes
        }

        // 4. Insere todos os itens via Batch Insert (otimização de banco de dados)
        if (request.itens.isNotEmpty()) {
            ItensOSTable.batchInsert(request.itens) { item ->
                this[ItensOSTable.osId] = novaOSId
                this[ItensOSTable.descricao] = item.descricao
                this[ItensOSTable.tipo] = item.tipo
                this[ItensOSTable.quantidade] = item.quantidade
                this[ItensOSTable.precoUnitario] = BigDecimal.valueOf(item.precoUnitario)
            }
        }

        novaOSId.value.toString()
    }

    suspend fun buscarPorId(id: String): OrdemServicoDTO? = dbQuery {
        val osRow = OrdensServicoTable.selectAll()
            .where { OrdensServicoTable.id eq UUID.fromString(id) }
            .singleOrNull() ?: return@dbQuery null

        val itens = ItensOSTable.selectAll()
            .where { ItensOSTable.osId eq UUID.fromString(id) }
            .map {
                ItemOSDTO(
                    id = it[ItensOSTable.id].value.toString(),
                    descricao = it[ItensOSTable.descricao],
                    tipo = it[ItensOSTable.tipo],
                    quantidade = it[ItensOSTable.quantidade],
                    precoUnitario = it[ItensOSTable.precoUnitario].toDouble()
                )
            }

        OrdemServicoDTO(
            id = osRow[OrdensServicoTable.id].value.toString(),
            oficinaId = osRow[OrdensServicoTable.oficinaId].value.toString(),
            veiculoId = osRow[OrdensServicoTable.veiculoId].value.toString(),
            status = osRow[OrdensServicoTable.status],
            kmEntrada = osRow[OrdensServicoTable.kmEntrada],
            valorTotal = osRow[OrdensServicoTable.valorTotal].toDouble(),
            observacoes = osRow[OrdensServicoTable.observacoes],
            itens = itens
        )
    }
}