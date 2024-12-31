package ru.somarov.berte.infrastructure.db.entity

import java.util.UUID

data class AuthenticationProvider(
    val id: UUID,
    val code: String
)
