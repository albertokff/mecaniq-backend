package com.mecaniq.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mecaniq.database.tables.UserRole
import java.util.Date

object JwtConfig {
    private const val SECRET = "mecaniq-chave-super-secreta-saas-2026"
    private const val ISSUER = "mecaniq.app"
    private const val AUDIENCE = "mecaniq-users"
    private const val VALIDITY_IN_MS = 3600_000L * 24 * 30

    private val algorithm = Algorithm.HMAC256(SECRET)

    val verifier = JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    fun generateToken(usuarioId: String, email: String, role: UserRole, oficinaId: String?): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("usuarioId", usuarioId)
            .withClaim("email", email)
            .withClaim("role", role.name)
            .withClaim("oficinaId", oficinaId ?: "")
            .withExpiresAt(Date(System.currentTimeMillis() + VALIDITY_IN_MS))
            .sign(algorithm)
    }
}