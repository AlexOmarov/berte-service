package ru.somarov.berte.infrastructure.db.entity

import java.util.UUID

data class RevokedAuthorization(
    val id: UUID,
    val token: String,
    val clientId: String
)
