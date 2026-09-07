package com.mecaniq.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

// 1. Tabela de Oficinas
object OficinasTable : UUIDTable("oficinas") {
    val nome = varchar("nome", 150)
    val cnpj = varchar("cnpj", 20).uniqueIndex()
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

enum class StatusOS {
    ABERTA,
    EM_ANDAMENTO,
    CONCLUIDA,
    CANCELADA
}

enum class TipoItem {
    PECA,
    SERVICO
}

object OrdensServicoTable : UUIDTable("ordens_servio") {
    val oficinaId = reference(name = "oficina_id", foreign = OficinasTable, onDelete = ReferenceOption.CASCADE)
    val veiculoId = reference(name = "veiculo_id", foreign = VeiculosTable, onDelete = ReferenceOption.RESTRICT)
    val status = enumerationByName(name = "status", length = 30, StatusOS::class).default(StatusOS.ABERTA)
    val kmEntrada = integer("km_entrada")
    val valorTotal = decimal("valor_total", 10, 2).default(java.math.BigDecimal.ZERO)
    val observacoes = varchar("observacoes", 500).nullable()
}

object ItensOSTable : UUIDTable("itens_os") {
    val osId = reference("os_id", OrdensServicoTable, onDelete = ReferenceOption.CASCADE)
    val descricao = varchar("descricao", 150)
    val tipo = enumerationByName("tipo", 20, TipoItem::class)
    val quantidade = integer("quantidade")
    val precoUnitario = decimal("preco_unitario", 10, 2)
}