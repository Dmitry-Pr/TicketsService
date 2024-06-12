package com.ticketingsystem.auth.service

import com.ticketingsystem.auth.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class DefaultUserDetailsService(
    private val defaultUserRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails =
        defaultUserRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User $email not found")
}