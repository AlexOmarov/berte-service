package ru.somarov.berte.infrastructure.db.repo

import ru.somarov.berte.infrastructure.db.DatabaseClient
import ru.somarov.berte.infrastructure.db.entity.Client
import java.util.UUID

class ClientRepo(private val client: DatabaseClient) {
    suspend fun findAll(): List<Client> {
        return client.transactional("Select * from client", mapOf()) { row, _ ->
            @Suppress("kotlin:S6518") // Cannot use [] due to r2dbc api
            Client(
                id = row.get("id", UUID::class.java)!!,
                login = row.get("login", String::class.java)!!,
                email = row.get("email", String::class.java)!!,
                password = row.get("password", String::class.java)!!,
            )
        }
    }
}
