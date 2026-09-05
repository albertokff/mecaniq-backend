package com.mecaniq.routes

import com.mecaniq.models.CriarOficinaRequest
import com.mecaniq.repositories.OficinaRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.oficinaRoutes(repository: OficinaRepository) {
    route("/oficinas") {
        // GET /oficinas -> Lista todos as oficinas
        get {
            val oficinas = repository.listarTodas()
            call.respond(HttpStatusCode.OK, oficinas)
        }

        // POST /oficinas -> Cria uma nova oficina
        post {
            val request = call.receive<CriarOficinaRequest>()
            val id = repository.inserir(request)
            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        }
    }
}