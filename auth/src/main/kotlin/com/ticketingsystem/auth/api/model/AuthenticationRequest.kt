package com.ticketingsystem.auth.api.model

data class AuthenticationRequest(
    val email: String,
    val password: String
)