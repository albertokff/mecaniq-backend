package com.mecaniq

import com.mecaniq.backend.database.DatabaseFactory
import com.mecaniq.routes.configureRouting
import io.ktor.server.application.Application

fun Application.rootModule() {
    DatabaseFactory.init()
    configureHttp()
    configureSerialization()
    configureRouting()
}
