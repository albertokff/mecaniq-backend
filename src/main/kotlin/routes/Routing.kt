package com.mecaniq.routes

import com.mecaniq.repositories.ClienteRepository
import com.mecaniq.repositories.OficinaRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val clienteRepository = ClienteRepository()
    val oficinaRepository = OficinaRepository()

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }

        clienteRoutes(clienteRepository)
        oficinaRoutes(oficinaRepository)
    }
}