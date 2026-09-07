package com.mecaniq.routes

import com.mecaniq.services.AlertaPreventivoService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.alertaRoutes(service: AlertaPreventivoService) {
    route("/veiculos/{veiculoId}/alertas") {
        get {
            val veiculoId = call.parameters["veiculoId"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, "ID do veículo ausente")

            try {
                val alertas = service.calcularAlertas(veiculoId)
                call.respond(HttpStatusCode.OK, alertas)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.NotFound, mapOf("erro" to (e.message ?: "Erro desconhecido")))
            }
        }
    }
}