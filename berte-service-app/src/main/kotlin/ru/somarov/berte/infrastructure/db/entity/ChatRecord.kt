package ru.somarov.berte.infrastructure.db.entity

import kotlinx.datetime.Instant
import java.util.UUID

data class ChatRecord(
    val id: UUID,
    val message: String,
    val creationDatetime: Instant,
    val gameSessionId: UUID,
    val heroId: UUID
)
