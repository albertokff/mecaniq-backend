package com.mecaniq.repositories

import com.mecaniq.backend.database.DatabaseFactory.dbQuery
import com.mecaniq.database.OficinasTable
import com.mecaniq.models.CriarOficinaRequest
import com.mecaniq.models.OficinaDTO
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll

class OficinaRepository {
    suspend fun listarTodas(): List<OficinaDTO> = dbQuery {
        OficinasTable.selectAll().map {
            OficinaDTO(
                id = it[OficinasTable.id].value.toString(),
                nome = it[OficinasTable.nome],
                cnpj = it[OficinasTable.cnpj],
                telefone = it[OficinasTable.telefone],
                email = it[OficinasTable.email]
            )
        }
    }

    suspend fun inserir(request: CriarOficinaRequest): String = dbQuery {
        val novoId = OficinasTable.insertAndGetId {
            it[nome] = request.nome
            it[cnpj] = request.cnpj.toString()
            it[telefone] = request.telefone
            it[email] = request.email.toString()
        }

        novoId.value.toString()
    }
}