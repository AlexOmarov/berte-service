package ru.somarov.berte.infrastructure.db.entity

import java.util.UUID

data class GameSession(
    val id: UUID,
    val name: String,
    val gameSessionStatusId: UUID
)
