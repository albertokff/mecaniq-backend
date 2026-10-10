package com.mecaniq.plugins

import com.mecaniq.security.JwtConfig
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity() {
    authentication {
        jwt("auth-jwt") {
            realm = "mecaniq.app"
            verifier(JwtConfig.verifier)
            validate { credential ->
                if (credential.payload.getClaim("usuarioId").asString().isNotEmpty()) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}