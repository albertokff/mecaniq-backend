package com.mecaniq.database.tables

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import kotlinx.datetime.Clock


enum class UserRole {
    SUPER_ADMIN,
    OFICINA_ADMIN,
    OFICINA_MECANICO,
    CLIENTE
}

object UsuariosTable : UUIDTable("usuarios") {
    val oficinaId = reference("oficina_id", OficinasTable).nullable()
    val nome = varchar("nome", 100)
    val email = varchar("email", 120).uniqueIndex()
    val senhaHash = varchar("senha_hash", 255)
    val role = enumerationByName("role", 20, UserRole::class)
    val ativo = bool("ativo").default(true)
    val criadoEm = datetime("criado_em").clientDefault {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
}

object OficinasTable : UUIDTable("oficinas") {
    val nome = varchar("nome", 150)
    val cnpj = varchar("cnpj", 20).uniqueIndex()
    val telefone = varchar("telefone", 20)
    val ativa = bool("ativa").default(true)
    val criadaEm = datetime("criada_em").clientDefault {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
}