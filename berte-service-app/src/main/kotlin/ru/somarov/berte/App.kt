package ru.somarov.berte

import io.ktor.server.netty.EngineMain
import reactor.core.publisher.Hooks

fun main(args: Array<String>) {
    Hooks.enableAutomaticContextPropagation()
    EngineMain.main(args)
}
