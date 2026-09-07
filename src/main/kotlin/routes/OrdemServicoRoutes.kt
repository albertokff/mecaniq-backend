package com.mecaniq.routes

import com.mecaniq.models.CriarOSRequest
import com.mecaniq.repositories.OrdemServicoRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.ordemServicoRoutes(repository: OrdemServicoRepository) {
    route("/ordens-servico") {
        post {
            val request = call.receive<CriarOSRequest>()
            val id = repository.criarOS(request)
            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "ID Ausente")
            val os = repository.buscarPorId(id)

            if (os != null) {
                call.respond(HttpStatusCode.OK, os)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("mensagem" to "OS não encontrada"))
            }
        }
    }
}