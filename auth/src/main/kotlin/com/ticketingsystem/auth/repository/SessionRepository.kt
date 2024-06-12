package com.ticketingsystem.auth.repository

import com.ticketingsystem.auth.model.Session
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionRepository : CrudRepository<Session, Int> {
    fun findByToken(token: String) : Session?
}