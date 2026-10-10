package com.mecaniq.repositories

import com.mecaniq.database.DatabaseFactory.dbQuery
import com.mecaniq.database.tables.OficinasTable
import com.mecaniq.database.tables.UserRole
import com.mecaniq.database.tables.UsuariosTable
import com.mecaniq.models.AuthResponse
import com.mecaniq.models.LoginRequest
import com.mecaniq.models.RegisterOficinaRequest
import com.mecaniq.security.JwtConfig
import com.mecaniq.security.PasswordHasher
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll

class AuthRepository {
    suspend fun registrarOficinaEAdmin(request: RegisterOficinaRequest): AuthResponse = dbQuery {
        val novaOficinaId = OficinasTable.insertAndGetId {
            it[nome] = request.nomeOficina
            it[cnpj] = request.cnpj ?: ""
            it[telefone] = request.telefone ?: ""
            it[ativa] = true
        }.value

        val hash = PasswordHasher.hashPassword(request.senhaAdmin)

        val novoUsuarioId = UsuariosTable.insertAndGetId {
            it[oficinaId] = novaOficinaId
            it[nome] = request.nomeAdmin
            it[email] = request.emailAdmin
            it[senhaHash] = hash
            it[role] = UserRole.OFICINA_ADMIN
            it[ativo] = true
        }.value

        val token = JwtConfig.generateToken(
            usuarioId = novoUsuarioId.toString(),
            email = request.emailAdmin,
            role = UserRole.OFICINA_ADMIN,
            oficinaId = novaOficinaId.toString()
        )

        AuthResponse(
            token = token,
            usuarioId = novoUsuarioId.toString(),
            nome = request.nomeAdmin,
            email = request.emailAdmin,
            role = UserRole.OFICINA_ADMIN,
            oficinaId = novaOficinaId.toString()
        )
    }

    suspend fun login(request: LoginRequest): AuthResponse? = dbQuery {
        val usuarioRow = UsuariosTable
            .selectAll()
            .where { UsuariosTable.email eq request.email }
            .singleOrNull() ?: return@dbQuery null

        val senhaHashSalva = usuarioRow[UsuariosTable.senhaHash]

        if (!PasswordHasher.verifyPassword(request.senha, senhaHashSalva)) {
            return@dbQuery null
        }

        val idUsuario = usuarioRow[UsuariosTable.id].value.toString()
        val nomeUsuario = usuarioRow[UsuariosTable.nome]
        val emailUsuario = usuarioRow[UsuariosTable.email]
        val roleUsuario = usuarioRow[UsuariosTable.role]
        val oficinaId = usuarioRow[UsuariosTable.oficinaId]?.value?.toString()

        val token = JwtConfig.generateToken(
            usuarioId = idUsuario,
            email = emailUsuario,
            role = roleUsuario,
            oficinaId = oficinaId
        )

        AuthResponse(
            token = token,
            usuarioId = idUsuario,
            nome = nomeUsuario,
            email = emailUsuario,
            role = roleUsuario,
            oficinaId = oficinaId,
        )
    }
}