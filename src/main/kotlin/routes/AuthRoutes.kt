package com.mecaniq.routes

import com.mecaniq.models.LoginRequest
import com.mecaniq.models.RegisterOficinaRequest
import com.mecaniq.repositories.AuthRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.response.respond

fun Route.authRoutes(authRepository: AuthRepository = AuthRepository()) {
    route("/auth") {
        post("/register") {
            try {
                val request = call.receive<RegisterOficinaRequest>()
                val response = authRepository.registrarOficinaEAdmin(request)
                call.respond(HttpStatusCode.Created, response)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("erro" to (e.localizedMessage ?: "Erro ao registrar oficina."))
                )
            }
        }

        post("/login") {
            try {
                val request = call.receive<LoginRequest>()
                val response = authRepository.login(request)

                if (response != null) {
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "E-mail ou senha inválidos."))
                }
            } catch (e :Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("erro" to "Falha ao processar o login.")
                )
            }
        }
    }
}