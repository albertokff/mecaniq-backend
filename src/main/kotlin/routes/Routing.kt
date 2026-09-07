package com.mecaniq.routes

import com.mecaniq.repositories.ClienteRepository
import com.mecaniq.repositories.OficinaRepository
import com.mecaniq.repositories.OrdemServicoRepository
import com.mecaniq.repositories.VeiculoRepository
import com.mecaniq.services.AlertaPreventivoService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val clienteRepository = ClienteRepository()
    val oficinaRepository = OficinaRepository()
    val veiculoRepository = VeiculoRepository()
    val ordemServicoRepository = OrdemServicoRepository()
    val alertaService = AlertaPreventivoService(veiculoRepository)

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }

        clienteRoutes(clienteRepository)
        oficinaRoutes(oficinaRepository)
        veiculoRoutes(veiculoRepository)
        ordemServicoRoutes(ordemServicoRepository)
        alertaRoutes(alertaService)
    }
}