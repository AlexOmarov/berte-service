package ru.somarov.berte.infrastructure.db.entity

import java.util.UUID

data class Hero(
    val id: UUID,
    val name: String,
    val clientId: String,
    val gameSessionId: String
)
