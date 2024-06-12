package com.ticketingsystem.auth.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class RegexConfig {
    @Value("\${regex.email}")
    lateinit var emailRegex: String

    @Value("\${regex.password}")
    lateinit var passwordRegex: String
}