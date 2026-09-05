package com.mecaniq.routes

import com.mecaniq.repositories.ClienteRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val clienteRepository = ClienteRepository()

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }

        clienteRoutes(clienteRepository)
    }
}