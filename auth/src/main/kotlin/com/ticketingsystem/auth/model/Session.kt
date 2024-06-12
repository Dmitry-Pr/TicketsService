package com.ticketingsystem.auth.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp

@Table("session")
class Session(
    @Id
    val id: Int? = null,
    @Column("user_id")
    private val userId: Int? = null,
    private val token: String,
    private val expires: Timestamp? = null,
)