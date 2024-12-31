package ru.somarov.berte.infrastructure.db.repo

import ru.somarov.berte.infrastructure.db.DatabaseClient
import ru.somarov.berte.infrastructure.db.entity.RevokedAuthorization
import java.util.UUID

class RevokedAuthorizationRepo(private val client: DatabaseClient) {
    suspend fun findAll(): List<RevokedAuthorization> {
        return client.transactional("Select * from revoked_authorization", mapOf()) { row, _ ->
            @Suppress("kotlin:S6518") // Cannot use [] due to r2dbc api
            RevokedAuthorization(
                id = row.get("id", UUID::class.java)!!,
                token = row.get("token", String::class.java)!!,
                clientId = row.get("clientId", String::class.java)!!,
            )
        }
    }
}
