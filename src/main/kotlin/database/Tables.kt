package com.mecaniq.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

// 1. Tabela de Oficinas
object OficinasTable : UUIDTable("oficinas") {
    val nome = varchar("nome", 150)
    val cnpjOuCpf = varchar("cnpj_cpf", 20).uniqueIndex()
    val telefone = varchar("telefone", 20)
    val email = varchar("email", 100).uniqueIndex()
}

// 2. Tabela de Clientes da Oficina
object ClientesTable : UUIDTable("clientes") {
    val oficinaId = reference("oficina_id", OficinasTable, onDelete = ReferenceOption.CASCADE)
    val nome = varchar("nome", 150)
    val telefone = varchar("telefone", 20)
    val email = varchar("email", 100).nullable()
    val cpf = varchar("cpf", 14).nullable()
}

// 3. Tabela de Veículos
object VeiculosTable : UUIDTable("veiculos") {
    val clienteId = reference("cliente_id", ClientesTable, onDelete = ReferenceOption.CASCADE)
    val placa = varchar("placa", 10).uniqueIndex()
    val modelo = varchar("modelo", 100)
    val marca = varchar("marca", 50)
    val ano = integer("ano")
    val kmAtual = integer("km_atual").default(0)
}