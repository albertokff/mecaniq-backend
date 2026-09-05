package com.mecaniq.routes

import com.mecaniq.models.CriarClienteRequest
import com.mecaniq.repositories.ClienteRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.clienteRoutes(repository: ClienteRepository) {
    route("/clientes") {
        // GET /clientes -> Lista todos os clientes
        get {
            val clientes = repository.listarTodos()
            call.respond(HttpStatusCode.OK, clientes)
        }

        // POST /clientes -> Cria um novo cliente
        post {
            val request = call.receive<CriarClienteRequest>()
            val id = repository.inserir(request)
            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        }
    }
}