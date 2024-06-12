package com.ticketingsystem.auth.repository

import com.ticketingsystem.auth.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Int> {
    fun findByEmail(email: String): User?
}