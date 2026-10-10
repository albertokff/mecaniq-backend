package com.mecaniq.routes

import com.mecaniq.models.AtualizarKmRequest
import com.mecaniq.models.CriarVeiculoRequest
import com.mecaniq.repositories.VeiculoRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.veiculoRoutes(repository: VeiculoRepository) {
    route("/veiculos") {
        // POST /veiculos -> Cadastra veículo
        post {
            try {
                val request = call.receive<CriarVeiculoRequest>()

                val criado = repository.inserir(request)

                if (criado != "") {
                    call.respond(HttpStatusCode.Created, true)
                } else {
                    call.respond(HttpStatusCode.BadRequest, false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, false)
            }
        }

        get("/placa/{placa}") {
            val placa = call.parameters["placa"] ?: return@get call.respond(HttpStatusCode.BadRequest)

            // Tenta buscar no banco
            val veiculo = repository.buscarPorPlaca(placa)

            if (veiculo != null) {
                call.respond(veiculo) // Retorna 200 OK com o JSON do veículo
            } else {
                // Quando a placa não existe, retorne 404 Not Found com ContentType JSON ou mensagem simples
                call.respond(HttpStatusCode.NotFound)
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