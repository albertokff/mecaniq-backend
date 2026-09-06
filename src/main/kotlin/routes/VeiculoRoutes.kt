package com.mecaniq.routes

import com.mecaniq.models.AtualizarKmRequest
import com.mecaniq.models.CriarVeiculoRequest
import com.mecaniq.repositories.VeiculoRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.veiculoRoutes(repository: VeiculoRepository) {
    route("/veiculos") {
        // POST /veiculos -> Cadastra veículo
        post {
            val request = call.receive<CriarVeiculoRequest>()
            val id = repository.inserir(request)
            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        }

        // GET /veiculos/placa/{placa} -> Busca por placa
        get("/placa/{placa}") {
            val placa = call.parameters["placa"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Placa ausente")
            val veiculo = repository.buscarPorPlaca(placa)

            if (veiculo != null) {
                call.respond(HttpStatusCode.OK, veiculo)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("mensagem" to "Veículo não encontrado"))
            }
        }

        // PATCH /veiculos/{id}/km -> Atualiza quilometragem
        patch("/{id}/km") {
            val id = call.parameters["id"] ?: return@patch call.respond(HttpStatusCode.BadRequest, "ID ausente")
            val request = call.receive<AtualizarKmRequest>()
            val atualizado = repository.atualizarKm(id, request)

            if (atualizado) {
                call.respond(HttpStatusCode.OK, mapOf("mensagem" to "Km atualizado com sucesso"))
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("mensagem" to "Veículo não encontrado"))
            }
        }
    }
}