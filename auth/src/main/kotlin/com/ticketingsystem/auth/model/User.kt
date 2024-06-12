package com.ticketingsystem.auth.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.sql.Timestamp

@Table("user")
class User(
    @Id
    private val id: Int? = null,
    private val nickname: String,
    private val email: String,
    private val password: String,
    private val created: Timestamp? = null

) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return password
    }

    fun getEmail(): String {
        return email
    }

    fun getId(): Int? {
        return id
    }

    override fun getUsername(): String {
        return nickname
    }

    fun comparePassword(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password, this.password)
    }

}