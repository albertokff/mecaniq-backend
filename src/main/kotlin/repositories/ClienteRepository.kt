package com.mecaniq.repositories

import com.mecaniq.backend.database.DatabaseFactory.dbQuery
import com.mecaniq.database.ClientesTable
import com.mecaniq.models.ClienteDTO
import com.mecaniq.models.CriarClienteRequest
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class ClienteRepository {
    suspend fun listarTodos(): List<ClienteDTO> = dbQuery {
        ClientesTable.selectAll().map {
            ClienteDTO(
                id = it[ClientesTable.id].value.toString(),
                oficinaId = it[ClientesTable.oficinaId].value.toString(),
                nome = it[ClientesTable.nome],
                telefone = it[ClientesTable.telefone],
                email = it[ClientesTable.email],
                cpf = it[ClientesTable.cpf]
            )
        }
    }

    suspend fun inserir(request: CriarClienteRequest): String = dbQuery {
        val novoId = ClientesTable.insertAndGetId {
            it[oficinaId] = UUID.fromString(request.oficinaId)
            it[nome] = request.nome
            it[telefone] = request.telefone
            it[email] = request.email
            it[cpf] = request.cpf
        }
        novoId.value.toString()
    }
}