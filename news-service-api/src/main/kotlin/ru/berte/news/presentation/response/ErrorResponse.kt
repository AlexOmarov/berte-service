package ru.somarov.berte.presentation.response

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(description = "Object which holds details of error which has been thrown")
data class ErrorResponse(val details: Map<String, String>) : java.io.Serializable
