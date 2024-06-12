package com.ticketingsystem.auth.api.model

data class RegistrationRequest(
    val nickname: String,
    val email: String,
    val password: String
)